package ru.otus.professional.yampolskiy.http.webserver.http;


public abstract class Http {
    protected HttpHeaders headers;
    protected String body;

    public Http() {
        this.headers = new HttpHeaders();
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public void addHeader(String name, String value) {
        headers.addHeader(name, value);
    }

}
