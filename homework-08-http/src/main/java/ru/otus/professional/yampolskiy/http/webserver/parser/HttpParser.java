package ru.otus.professional.yampolskiy.http.webserver.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpHeaders;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpMethod;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class HttpParser {
    private static final Logger logger = LogManager.getLogger(HttpParser.class);

    public static HttpRequest parse(InputStream in, Socket socket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        String requestLine = reader.readLine();

        if (requestLine == null || requestLine.isBlank()) {
            logger.warn("Получен пустой HTTP-запрос");
            return null;
        }

        HttpRequest httpRequest = new HttpRequest(in, socket);

        try {
            parseRequestLine(requestLine, httpRequest);
        } catch (IllegalArgumentException e) {
            logger.error("Некорректная первая строка запроса: {}", requestLine, e);
            throw new IOException("Некорректная первая строка HTTP-запроса", e);
        }

        HttpHeaders headers = new HttpHeaders();
        String line;
        while ((line = reader.readLine()) != null && !line.isBlank()) {
            int separatorIndex = line.indexOf(":");
            if (separatorIndex > 0) {
                String name = line.substring(0, separatorIndex).trim();
                String value = line.substring(separatorIndex + 1).trim();
                headers.addHeader(name, value);
            } else {
                logger.warn("Некорректный заголовок: {}", line);
            }
        }
        httpRequest.setHeaders(headers);

        logger.debug("Запрос успешно распознан: {}", httpRequest);
        return httpRequest;
    }

    private static void parseRequestLine(String requestLine, HttpRequest httpRequest) {
        int methodEndIndex = requestLine.indexOf(' ');
        if (methodEndIndex == -1) {
            throw new IllegalArgumentException("Некорректная строка запроса: " + requestLine);
        }
        String method = requestLine.substring(0, methodEndIndex).toUpperCase();
        httpRequest.setMethod(HttpMethod.valueOf(method));

        int uriStartIndex = methodEndIndex + 1;
        int uriEndIndex = requestLine.indexOf(' ', uriStartIndex);
        if (uriEndIndex == -1) {
            throw new IllegalArgumentException("Некорректная строка запроса: " + requestLine);
        }
        String rawUri = requestLine.substring(uriStartIndex, uriEndIndex);

        int queryIndex = rawUri.indexOf('?');
        if (queryIndex != -1) {
            httpRequest.setUri(URI.create(rawUri.substring(0, queryIndex))); // URI без параметров
            String query = rawUri.substring(queryIndex + 1);
            parseQueryParameters(query, httpRequest);
        } else {
            httpRequest.setUri(URI.create(rawUri));
        }

        String protocolVersion = requestLine.substring(uriEndIndex + 1);
        httpRequest.setProtocolVersion(protocolVersion);
    }


    private static void parseQueryParameters(String query, HttpRequest httpRequest) {
        int start = 0;
        while (start < query.length()) {
            int equalsIndex = query.indexOf('=', start);
            int ampIndex = query.indexOf('&', start);

            if (equalsIndex == -1 || (ampIndex != -1 && ampIndex < equalsIndex)) {
                throw new IllegalArgumentException("Некорректный параметр запроса: " + query.substring(start));
            }

            String key = query.substring(start, equalsIndex);
            String value = (ampIndex == -1)
                    ? query.substring(equalsIndex + 1)
                    : query.substring(equalsIndex + 1, ampIndex);

            httpRequest.addRequestParameter(
                    decodeUrlComponent(key),
                    decodeUrlComponent(value)
            );

            start = (ampIndex == -1) ? query.length() : ampIndex + 1;
        }
    }


    private static String decodeUrlComponent(String component) {
        try {
            return java.net.URLDecoder.decode(component, StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.warn("Ошибка декодирования параметра: {}", component, e);
            return component;
        }
    }
}




