package ru.otus.professional.yampolskiy.http.webserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpHeader;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpRequest;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpResponse;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpStatus;
import ru.otus.professional.yampolskiy.http.webserver.interfaces.RequestHandler;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServletDispatcher implements RequestHandler {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Override
    public HttpResponse execute(HttpRequest request) throws IOException {
        String body = workWithBody(request);
        logger.debug("[ОТЛАДКА]  Тело запроса: {}",body);
        return new HttpResponse.Builder()
                .setProtocolVersion(request.getProtocolVersion())
                .setStatus(HttpStatus.OK)
                .addHeader(HttpHeader.CONTENT_TYPE, "text/plain")
                .setBody(body)
                .build();
    }

    private String workWithBody(HttpRequest request) throws IOException {
        StringBuilder result = new StringBuilder();
        InputStream bodyStream = request.getBodyStream();
        try {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bodyStream.read(buffer)) != -1) {
                result.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            logger.error("[ОТЛАДКА]  Ошибка при чтении тела запроса",e);
            throw new IOException("Ошибка при чтении тела запроса", e);
        }
        logger.debug("[ОТЛАДКА]  Размер тела запроса {}", result.toString().getBytes().length);
        return result.toString();
    }


}





