package br.com.lucas.baseapp.solr;

import br.com.lucas.baseapp.model.ObjectReturn;
import br.com.lucas.baseapp.model.SolrResult;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class SolrService {

    @Autowired
    private SolrClient solrClient;

    @Value("${solr.enable}")
    private Boolean enable;

    public Boolean isEnabled() {
        return enable != null && enable;
    }

    private String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public ObjectReturn add(MultipartFile uploadedFile, String fileId, String title, String[] keywords) {
        ObjectReturn objectReturn = new ObjectReturn();
        objectReturn.setResult(true);

        if (isEnabled()) {
            File file = null;
            String uuid = generateUUID();

            try {

                file = File.createTempFile(uuid, "tmp");
                file.deleteOnExit();

                uploadedFile.transferTo(file);

                ContentStreamUpdateRequest contentStreamUpdateRequest = new ContentStreamUpdateRequest("/update/extract");

                contentStreamUpdateRequest.addFile(file, uploadedFile.getContentType());

                ModifiableSolrParams modifiableSolrParams = new ModifiableSolrParams();

                modifiableSolrParams.add("literal.id", fileId);

                if (title != null && !title.isEmpty()) {
                    modifiableSolrParams.add("literal.title", title);
                }

                if (keywords != null && keywords.length > 0) {
                    for (String keyword : keywords) {
                        modifiableSolrParams.add("literal.keywords", keyword);
                    }
                }

                contentStreamUpdateRequest.setParams(modifiableSolrParams);

                contentStreamUpdateRequest.setAction(AbstractUpdateRequest.ACTION.COMMIT, false, false, true);

                contentStreamUpdateRequest.process(solrClient);

                file.delete();

                objectReturn.setData(fileId);

            } catch (IOException e) {
                objectReturn.setResult(false);
                objectReturn.setData(new SolrException("Could send file to index", e).toString());
            } catch (SolrServerException e) {
                objectReturn.setResult(false);
                objectReturn.setData(new SolrException("Could send file to index", e).toString());
            } catch (Exception e) {
                objectReturn.setResult(false);
                objectReturn.setData(new SolrException("Could send file to index", e).toString());
            }
            finally {
                if(file != null && file.exists()) {
                    file.delete();
                }
            }
        }

        return objectReturn;
    }

    public SolrDocumentList query(String query) {
        SolrDocumentList solrDocumentList = new SolrDocumentList();
        if (isEnabled()) {
            try {
                SolrQuery solrQuery = new SolrQuery();

                solrQuery.setQuery(query);
                solrQuery.setStart(0);
                solrQuery.set("defType", "edismax");

                QueryResponse queryResponse = solrClient.query(solrQuery);

                solrDocumentList = queryResponse.getResults();

                return solrDocumentList;
            } catch (SolrServerException e) {
                throw new SolrException("Could execute query", e);
            } catch (IOException e) {
                throw new SolrException("Could execute query", e);
            }
        }
        return solrDocumentList;
    }

    public List<SolrResult> queryMoreLikeThis(String id) {
        List<SolrResult> solrResults = new ArrayList<SolrResult>();
        if (isEnabled()) {
            try {
                SolrQuery solrQuery = new SolrQuery();

                solrQuery.setQuery("id:" + id);

                solrQuery.set("defType", "edismax");

                solrQuery.setMoreLikeThis(true)
                        .setMoreLikeThisCount(4)
                        .setMoreLikeThisMinTermFreq(1)
                        .setMoreLikeThisMinDocFreq(1)
                        .setMoreLikeThisBoost(true)
                        .setMoreLikeThisFields("content", "keywords", "title");

                solrQuery.addField("score");

                solrQuery.addField("title");

                solrQuery.addField("keywords");

                solrQuery.addField("id");

                QueryResponse queryResponse = solrClient.query(solrQuery);

                NamedList<SolrDocumentList> namedList = queryResponse.getMoreLikeThis();

                if (namedList.size() > 0) {
                    Iterator<Map.Entry<String, SolrDocumentList>> iterator = namedList.iterator();

                    while (iterator.hasNext()) {
                        Map.Entry<String, SolrDocumentList> next = iterator.next();

                        SolrDocumentList solrDocumentList = next.getValue();


                        for (int i = 0; i < solrDocumentList.size(); i++) {
                            SolrResult solrResult = new SolrResult();

                            SolrDocument solrDocument = solrDocumentList.get(i);

                            solrResult.setId(solrDocument.getFieldValue("id").toString());

                            solrResult.setScore(solrDocument.getFieldValue("score").toString());

                            SolrDocumentList queryData = query("id:" + solrResult.getId());

                            List<String> keywords = new ArrayList<String>();

                            Collection<Object> keywordsField = queryData.get(0).getFieldValues("keywords");

                            if (keywordsField != null) {
                                for (Object keyword : keywordsField) {
                                    keywords.add(keyword.toString());
                                }
                            }

                            if (keywords.size() > 0) {
                                solrResult.setTags(keywords);
                            }

                            Collection<Object> titleField = queryData.get(0).getFieldValues("title");

                            if (titleField != null) {
                                for (Object title : titleField) {
                                    solrResult.setTitle(title.toString());
                                }
                            }

                            solrResults.add(solrResult);
                        }

                    }


                }


            } catch (SolrServerException e) {
                throw new SolrException("Could execute query", e);
            } catch (IOException e) {
                throw new SolrException("Could execute query", e);
            }
        }
        return solrResults;
    }

    public List<SolrResult> queryHighlight(String query) {
        List<SolrResult> solrResults = new ArrayList<SolrResult>();
        if (isEnabled()) {
            try {
                SolrQuery solrQuery = new SolrQuery();

                solrQuery.setQuery(query);

                solrQuery.setHighlight(true);

                solrQuery.setHighlightSnippets(3);

                solrQuery.setHighlightFragsize(140);


                /*solrQuery.setHighlightSimplePre("<strong>");

                solrQuery.setHighlightSimplePost("</strong>");*/

                solrQuery.setParam("hl.fl", "*");

                solrQuery.setParam("hl.method=unified");

                solrQuery.addField("id");

                solrQuery.addField("score");

                solrQuery.addField("title");

                QueryResponse queryResponse = solrClient.query(solrQuery);

                SolrDocumentList solrDocumentsList = queryResponse.getResults();

                for (int i = 0; i < solrDocumentsList.size(); i++) {
                    SolrResult solrResult = new SolrResult();
                    SolrDocument solrDocument = solrDocumentsList.get(i);

                    solrResult.setId(solrDocument.getFieldValue("id").toString());

                    solrResult.setScore(solrDocument.getFieldValue("score").toString());

                    for (Object title : solrDocument.getFieldValues("title")) {
                        solrResult.setTitle(title.toString());
                    }

                    solrResult.setHighlight(queryResponse.getHighlighting().get(solrResult.getId()));

                    solrResults.add(solrResult);
                }

            } catch (SolrServerException e) {
                throw new SolrException("Could execute query", e);
            } catch (IOException e) {
                throw new SolrException("Could execute query", e);
            }
        }
        return solrResults;
    }

    public void delete(String id) {
        if (isEnabled()) {
            try {

                solrClient.deleteById(id);
                solrClient.commit(false,false,true);

            } catch (SolrServerException e) {
                throw new SolrException("Could delete file index", e);
            } catch (IOException e) {
                throw new SolrException("Could delete file index", e);
            }
        }
    }

    public void deleteAll() {
        if (isEnabled()) {
            try {
                solrClient.deleteByQuery("*:*");
                solrClient.commit();
            } catch (SolrServerException e) {
                throw new SolrException("Could delete all index", e);
            } catch (IOException e) {
                throw new SolrException("Could delete all index", e);
            }
        }
    }

}