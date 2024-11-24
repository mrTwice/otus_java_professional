package ru.otus.professional.yampolskiy.http.webserver.http;


public abstract class Http {
    protected HttpHeaders headers;
    protected String body;

    public Http() {
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

}
