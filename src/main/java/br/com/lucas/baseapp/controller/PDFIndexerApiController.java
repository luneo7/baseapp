package br.com.lucas.baseapp.controller;

import br.com.lucas.baseapp.model.File;
import br.com.lucas.baseapp.model.ObjectReturn;
import br.com.lucas.baseapp.service.FileService;
import br.com.lucas.baseapp.solr.SolrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/api")
public class PDFIndexerApiController {

    @Autowired
    private SolrService solrService;

    @Autowired
    FileService fileService;

    @RequestMapping(value = "/files", method = RequestMethod.GET)
    @ResponseBody
    public Iterable<File> getAllFiles() {
        return fileService.getAll();
    }

    @RequestMapping(value = "/files/{id}", method = RequestMethod.GET)
    @ResponseBody
    public File getFileById(@PathVariable(value = "id") Long id) {
        return fileService.get(id);
    }

    @RequestMapping(value = "/processfile", method = RequestMethod.POST)
    @ResponseBody
    public ObjectReturn processFile(@RequestParam("file") MultipartFile uploadedFile, @RequestParam(required = false) String title, @RequestParam(required = false) String tags){

        ObjectReturn objectReturn = new ObjectReturn();

        try{

            String[] keywords = null;
            if(tags != null && !tags.isEmpty()){
                keywords = tags.split("\\|");
            }

            if (uploadedFile.getContentType().toLowerCase().contains("pdf")) {
                File file = new File();
                file.setFileTitle(title);
                file.setFileName(uploadedFile.getOriginalFilename());
                fileService.save(file);
                objectReturn = solrService.add(uploadedFile, file.getFileId().toString(), title, keywords);
            } else {
                objectReturn.setResult(true);
            }


        } catch (Exception e) {
            e.printStackTrace();
            ObjectReturn objectReturn1 = new ObjectReturn();
            objectReturn1.setResult(false);
            return objectReturn1;
        }

        return objectReturn;
    }


    @RequestMapping(value = "/removebykey", method = RequestMethod.POST)
    @ResponseBody
    public ObjectReturn removeByKey(@RequestParam String key) {
        ObjectReturn objectReturn = new ObjectReturn();

        try {
            solrService.delete(key);
            fileService.remove(Long.parseLong(key));
            objectReturn.setResult(true);
            objectReturn.setData(key);
        } catch (Exception e) {
            e.printStackTrace();
            objectReturn.setResult(false);
        }

        return objectReturn;
    }

    @RequestMapping(value = "/solrquery", method = RequestMethod.POST)
    @ResponseBody
    public ObjectReturn list(@RequestParam String query) {
        ObjectReturn objectReturn = new ObjectReturn();
        try {
            objectReturn.setData(solrService.query(query));
            objectReturn.setResult(true);
        } catch (Exception e) {
            e.printStackTrace();
            objectReturn.setResult(false);
        }

        return objectReturn;
    }

    @RequestMapping(value = "/solrqueryhighlight", method = RequestMethod.POST)
    @ResponseBody
    public ObjectReturn listHighlight(@RequestParam String query) {
        ObjectReturn objectReturn = new ObjectReturn();
        try {
            objectReturn.setData(solrService.queryHighlight(query));
            objectReturn.setResult(true);
        } catch (Exception e) {
            e.printStackTrace();
            objectReturn.setResult(false);
        }

        return objectReturn;
    }

    @RequestMapping(value = "/solrquerymorelikethis", method = RequestMethod.POST)
    @ResponseBody
    public ObjectReturn listMoreLikeThis(@RequestParam String id) {

        ObjectReturn objectReturn = new ObjectReturn();
        try {
            objectReturn.setData(solrService.queryMoreLikeThis(id));
            objectReturn.setResult(true);
        } catch (Exception e) {
            e.printStackTrace();
            objectReturn.setResult(false);
        }

        return objectReturn;
    }
}
