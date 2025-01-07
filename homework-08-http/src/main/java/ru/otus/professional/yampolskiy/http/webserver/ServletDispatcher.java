package ru.otus.professional.yampolskiy.http.webserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpHeader;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpRequest;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpResponse;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpStatus;
import ru.otus.professional.yampolskiy.http.webserver.interfaces.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class ServletDispatcher implements RequestHandler {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    public ServletDispatcher() {
        logger.debug("[ОТЛАДКА]  Инициализация ServletDispatcher");
    }

    @Override
    public HttpResponse execute(HttpRequest request) throws IOException {
        logger.debug("[ОТЛАДКА]  Получен запрос: {}", request.toString());
        String body = workWithBody(request);
        logger.debug("[ОТЛАДКА]  Тело запроса: {}", body  );

        return new HttpResponse.Builder()
                .setProtocolVersion(request.getProtocolVersion())
                .setStatus(HttpStatus.OK)
                .addHeader(HttpHeader.CONTENT_TYPE, "text/plain")
                .setBody(body)
                .build();
    }

    private String workWithBody(HttpRequest request) throws IOException {
        StringBuilder result = new StringBuilder();
        byte[] buffer = new byte[1024];
        int bytesRead;

        // Чтение данных из SequenceInputStream
        while ((bytesRead = request.getBodyStream().read(buffer)) != -1) {
            result.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
        }

        return result.toString();
    }
}





