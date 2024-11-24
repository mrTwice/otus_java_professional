package ru.otus.professional.yampolskiy.http.webserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpHeader;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpRequest;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpResponse;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpStatus;
import ru.otus.professional.yampolskiy.http.webserver.interfaces.RequestHandler;

public class ServletDispatcher implements RequestHandler {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    public ServletDispatcher() {
        logger.debug("Инициализация ServletDispatcher");
    }

    @Override
    public HttpResponse execute(HttpRequest request) {
        // Log the request details
        logger.info("Получен запрос: {}", request.toString());

        HttpResponse response = new HttpResponse.Builder()
                .setProtocolVersion(request.getProtocolVersion())
                .setStatus(HttpStatus.OK)
                .addHeader(HttpHeader.CONTENT_TYPE, "text/plain")
                .setBody("Запрос получен")
                .build();

        return response;
    }
}





