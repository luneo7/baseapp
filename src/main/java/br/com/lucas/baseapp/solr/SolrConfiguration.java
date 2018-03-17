package br.com.lucas.baseapp.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SolrConfiguration {

    @Value("${solr.host}")
    private String host;

    @Value("${solr.mode}")
    private String mode;

    @Value("${solr.collection}")
    private String collection;

    @Value("${solr.enable}")
    private Boolean enable;

    @Bean
    public SolrClient solrClient() {
        if(enable != null && enable) {
            if (mode != null && mode.toLowerCase().equals("cloud")) {
                CloudSolrClient cloudSolrClient = new CloudSolrClient.Builder().withZkHost(host).build();
                cloudSolrClient.setDefaultCollection(collection);
                return cloudSolrClient;
            } else {
                return new HttpSolrClient.Builder(host).build();
            }
        }
        else {
            return null;
        }
    }
}

