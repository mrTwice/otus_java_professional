package ru.otus.professional.yampolskiy.http.webserver.http;

import java.util.HashMap;
import java.util.Map;

public class HttpHeaders {
    private Map<String, String> headers;

    public HttpHeaders() {
        this.headers = new HashMap<>();
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void removeHeader(String key) {
        headers.remove(key);
    }

    public String getHeader(HttpHeader header) {
        return headers.get(header.getHeaderName());
    }

    public Map<String, String> getAllHeaders() {
        return headers;
    }

    @Override
    public String toString() {
        return headers.toString();
    }
}
