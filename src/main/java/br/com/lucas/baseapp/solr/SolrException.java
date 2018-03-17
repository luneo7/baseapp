package br.com.lucas.baseapp.solr;

public class SolrException extends RuntimeException {

    public SolrException(String message) {
        super(message);
    }

    public SolrException(String message, Throwable cause) {
        super(message, cause);
    }

}
