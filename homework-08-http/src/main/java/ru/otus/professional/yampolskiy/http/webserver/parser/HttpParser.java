package ru.otus.professional.yampolskiy.http.webserver.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpRequest;

import java.util.Arrays;

public class HttpParser {
    private static final Logger logger = LogManager.getLogger(HttpParser.class);

    public static void parseHeaders(HttpRequest httpRequest, String rawHeaders) {
        String[] lines = rawHeaders.split("\r\n");
        logger.debug("[ОТЛАДКА]  RequestLine: {}", lines[0] );
        String[] requestLine = lines[0].split(" ");
        logger.debug("[ОТЛАДКА]  {}", Arrays.asList(requestLine));
        logger.debug("[ОТЛАДКА]  Method: {}", requestLine[0] );
        logger.debug("[ОТЛАДКА]  Uri: {}", requestLine[1] );
        logger.debug("[ОТЛАДКА]  ProtocolVersion: {}", requestLine[2] );
        httpRequest.setMethod(requestLine[0].trim());
        httpRequest.setUri(requestLine[1].trim());
        httpRequest.setProtocolVersion(requestLine[2].trim());

        for (int i = 1; i < lines.length; i++) {
            if (lines[i].contains(":")) {
                httpRequest.addHeader(
                        lines[i].substring(0, lines[i].indexOf(':')).trim(),
                        lines[i].substring(lines[i].indexOf(':') + 1).trim());
            }
        }
    }
}




