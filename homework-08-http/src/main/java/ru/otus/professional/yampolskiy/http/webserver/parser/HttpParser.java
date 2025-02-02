package ru.otus.professional.yampolskiy.http.webserver.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpRequest;

import java.net.URI;

public class HttpParser {
    private static final Logger logger = LogManager.getLogger(HttpParser.class);

    public static void parse(HttpRequest httpRequest, String rawHeaders) {
        int firstLineEnd = rawHeaders.indexOf("\r\n");
        parseRequestLine(rawHeaders.substring(0, firstLineEnd), httpRequest);
        parseHeaders(httpRequest, rawHeaders, firstLineEnd + 2);
        logger.debug("[ЗАПРОС] {}", httpRequest.toString());
    }

    private static void parseRequestLine(String requestLine, HttpRequest httpRequest) {
        int methodEnd = requestLine.indexOf(' ');
        httpRequest.setMethod(requestLine.substring(0, methodEnd));

        int uriStart = methodEnd + 1;
        int uriEnd = requestLine.indexOf(' ', uriStart);
        String uriPart = requestLine.substring(uriStart, uriEnd);

        processUriAndParams(httpRequest, uriPart);
        httpRequest.setProtocolVersion(requestLine.substring(uriEnd + 1));
    }

    private static void processUriAndParams(HttpRequest httpRequest, String uriPart) {
        int paramsStart = uriPart.indexOf('?');
        if(paramsStart == -1) {
            httpRequest.setUri(URI.create(uriPart));
            return;
        }

        httpRequest.setUri(URI.create(uriPart.substring(0, paramsStart)));
        parseParams(uriPart, paramsStart + 1, httpRequest);
    }

    private static void parseParams(String uriPart, int paramsStart, HttpRequest httpRequest) {
        int pos = paramsStart;
        while(pos < uriPart.length()) {
            int eqPos = uriPart.indexOf('=', pos);
            if(eqPos == -1) break;

            int ampPos = uriPart.indexOf('&', eqPos);
            if(ampPos == -1) ampPos = uriPart.length();

            String key = uriPart.substring(pos, eqPos);
            String value = eqPos + 1 < ampPos
                    ? uriPart.substring(eqPos + 1, ampPos)
                    : "";

            httpRequest.addRequestParameter(key, value);
            pos = ampPos + 1;
        }
    }

    private static void parseHeaders(HttpRequest httpRequest, String headers, int startPos) {
        int pos = startPos;
        while(pos < headers.length()) {
            int lineEnd = headers.indexOf("\r\n", pos);
            if(lineEnd == -1) lineEnd = headers.length();

            String headerLine = headers.substring(pos, lineEnd);
            processHeaderLine(headerLine, httpRequest);

            pos = lineEnd + 2;
            if(pos >= headers.length()) break;
        }
    }

    private static void processHeaderLine(String headerLine, HttpRequest httpRequest) {
        int colonPos = headerLine.indexOf(':');
        if(colonPos == -1) return;

        String name = trimSubstring(headerLine, 0, colonPos);
        String value = trimSubstring(headerLine, colonPos + 1, headerLine.length());
        httpRequest.addHeader(name, value);
    }

    private static String trimSubstring(String str, int start, int end) {
        while(start < end && Character.isWhitespace(str.charAt(start))) start++;
        while(end > start && Character.isWhitespace(str.charAt(end - 1))) end--;
        return str.substring(start, end);
    }
}




