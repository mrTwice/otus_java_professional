package ru.otus.professional.yampolskiy.http.webserver.http;

import java.io.InputStream;
import java.net.Socket;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest extends Http{
    private HttpMethod method;
    private URI uri;
    private String protocolVersion;
    private Map<String, String> requestParameters;
    private Socket socket;
    private InputStream bodyStream;

    public HttpRequest(InputStream bodyStream, Socket socket) {
        this.socket = socket;
        this.bodyStream = bodyStream;
        this.requestParameters = new HashMap<>();
    }

    public HttpRequest() {
        requestParameters = new HashMap<>();
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setBodyStream(InputStream bodyStream) {
        this.bodyStream = bodyStream;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public void setMethod(String method) {
        this.method = HttpMethod.fromString(method);
    }


    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public void setUri(String uri) {
        this.setUri(URI.create(uri));
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public Map<String, String> getRequestParameters() {
        return requestParameters;
    }

    public String getContentType() {
        return getHeader(HttpHeader.CONTENT_TYPE);
    }

    public int getContentLength() {
        String contentLength = headers.getHeader(HttpHeader.CONTENT_LENGTH);
        return Integer.parseInt(contentLength);
    }



    public void setRequestParameters(Map<String, String> requestParameters) {
        this.requestParameters = requestParameters;
    }

    public void addRequestParameter(String key, String value) {
        requestParameters.put(key, value);
    }

    public String getRoutingKey() {
        return method + " " + uri;
    }

    public boolean containsParameter(String key) {
        return requestParameters.containsKey(key);
    }

    public String getParameter(String key) {
        return requestParameters.get(key);
    }

    public InputStream getBodyStream() {
        return bodyStream;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nHttpRequest {\n");
        sb.append("  method=").append(method != null ? method : "null").append(",\n");
        sb.append("  uri=").append(uri != null ? uri : "null").append(",\n");
        sb.append("  protocolVersion='").append(protocolVersion != null ? protocolVersion : "null").append("',\n");
        sb.append("  requestParameters=").append(requestParameters != null ? requestParameters : "null").append(",\n");
        sb.append("  headers=").append(headers != null ? headers : "null").append(",\n");
//        sb.append("  body='").append(body != null ? body : "null").append("'\n");
        sb.append("}");
        return sb.toString();
    }

    public String getHeader(HttpHeader httpHeader) {
        return httpHeader.getHeaderName();
    }
}
