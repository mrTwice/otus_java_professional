package ru.otus.professional.yampolskiy.http.webserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpHeader;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpRequest;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpResponse;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpStatus;
import ru.otus.professional.yampolskiy.http.webserver.interfaces.RequestHandler;

import java.io.IOException;
import java.io.InputStream;

public class ServletDispatcher implements RequestHandler {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Override
    public HttpResponse execute(HttpRequest request) throws IOException {
        String body = workWithBody(request);
        return new HttpResponse.Builder()
                .setProtocolVersion(request.getProtocolVersion())
                .setStatus(HttpStatus.OK)
                .addHeader(HttpHeader.CONTENT_TYPE, "text/plain")
                .setBody(body)
                .build();
    }

    /**
     * Имитация обработки тела запроса, для того, чтобы можно было убедится, что оно долетает в целости до ServletDispatcher
     */
    private String workWithBody(HttpRequest request) throws IOException {
        InputStream bodyStream = request.getBodyStream();
        String rawRequest ="";
        try {
            byte[] buffer = new byte[8192];
            int n = bodyStream.read(buffer);
            if (n > 0) {
                rawRequest = new String(buffer, 0, n);
            }
        } catch (IOException e) {
            logger.error("[ОТЛАДКА]  Ошибка при чтении тела запроса", e);
            throw new IOException("Ошибка при чтении тела запроса", e);
        }
        return rawRequest;
    }


}





