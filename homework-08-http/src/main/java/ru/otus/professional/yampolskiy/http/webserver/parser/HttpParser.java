package ru.otus.professional.yampolskiy.http.webserver.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpHeaders;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpMethod;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpRequest;

import java.net.URI;

public class HttpParser {
    private static final Logger logger = LogManager.getLogger(HttpParser.class);
    private static final int OFFSET = 1;

    public static void parse(HttpRequest httpRequest, String rawHeaders) {
        parseRequestLine(rawHeaders.substring(0, rawHeaders.indexOf("\r\n")), httpRequest);
        getHeaders(httpRequest, rawHeaders.substring(rawHeaders.indexOf("\r\n") + 2));
        logger.debug("[ЗАПРОС] {}", httpRequest.toString());
    }

    private static void parseRequestLine(String requestLines, HttpRequest httpRequest) {
        parseProtocolVersion(
                requestLines,
                httpRequest,
                parseUriAndParameters(
                        requestLines,
                        httpRequest,
                        parseMethod(
                                requestLines,
                                httpRequest
                        )
                )
        );
    }

    private static void parseProtocolVersion(String requestLines, HttpRequest httpRequest, int uriEndIndex) {
        httpRequest.setProtocolVersion(requestLines.substring(uriEndIndex));
    }

    private static int parseMethod(String requestLines, HttpRequest httpRequest) {
        httpRequest.setMethod(requestLines.substring(0, requestLines.indexOf(' ')));
        return requestLines.indexOf(' ') + OFFSET;
    }

    private static int parseUriAndParameters(String requestLines, HttpRequest httpRequest, int methodEndIndex) {
        if (requestLines.substring(methodEndIndex, requestLines.indexOf(' ', methodEndIndex)).indexOf('?') != -1) {
            httpRequest.setUri(URI.create(requestLines.substring(methodEndIndex, requestLines.indexOf(' ', methodEndIndex)).substring(0, requestLines.substring(methodEndIndex, requestLines.indexOf(' ', methodEndIndex)).indexOf('?'))));
            for (String pair : requestLines.substring(methodEndIndex, requestLines.indexOf(' ', methodEndIndex)).substring(requestLines.substring(methodEndIndex, requestLines.indexOf(' ', methodEndIndex)).indexOf('?') + 1).split("&")) {
                if (pair.indexOf('=') != -1) {
                    httpRequest.addRequestParameter(
                            pair.substring(0, pair.indexOf('=')),
                            pair.substring(pair.indexOf('=') + 1)
                    );
                }
            }
        } else {
            httpRequest.setUri(URI.create(requestLines.substring(methodEndIndex, requestLines.indexOf(' ', methodEndIndex))));
        }

        return requestLines.indexOf(' ', methodEndIndex) + OFFSET;
    }



    private static void getHeaders(HttpRequest httpRequest, String headers) {
        int start = 0;
        int end;

        while ((end = headers.indexOf("\r\n", start)) != -1) {
            if (headers.indexOf(':', start) != -1 && headers.indexOf(':', start) < end) {
                httpRequest.addHeader(
                        extractTrimmedSubstring(headers, start, headers.indexOf(':', start)),
                        extractTrimmedSubstring(headers, headers.indexOf(':', start) + 1, end));
            }
            start = end + 2;
        }

        if (start < headers.length()) {
            if (headers.indexOf(':', start) != -1) {
                httpRequest.addHeader(
                        extractTrimmedSubstring(headers, start, headers.indexOf(':', start)),
                        extractTrimmedSubstring(headers, headers.indexOf(':', start) + 1,
                                headers.length()
                        )
                );
            }
        }
    }

    private static String extractTrimmedSubstring(String str, int start, int end) {
        while (start < end && Character.isWhitespace(str.charAt(start))) start++;
        while (end > start && Character.isWhitespace(str.charAt(end - 1))) end--;
        return str.substring(start, end);
    }



}




