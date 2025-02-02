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
import java.nio.charset.StandardCharsets;

public class ServletDispatcher implements RequestHandler {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Override
    public HttpResponse execute(HttpRequest request) throws IOException {
        if(request.getHeaders().getAllHeaders().containsKey("Content-Type")) {

            HttpResponse httpResponse = new HttpResponse.Builder()
                    .setProtocolVersion(request.getProtocolVersion())
                    .setStatus(HttpStatus.OK)
                    .addHeader(HttpHeader.CONTENT_TYPE, "application/json")
                    .setBody(workWithBody(request))
                    .build();
            logger.info(httpResponse.toString());
            return httpResponse;
        } else {
            HttpResponse httpResponse = new HttpResponse.Builder()
                    .setProtocolVersion(request.getProtocolVersion())
                    .setStatus(HttpStatus.NO_CONTENT)
                    .addHeader(HttpHeader.CONTENT_LENGTH, "0")
                    .setBody("")
                    .build();
            logger.info(httpResponse.toString());
            return httpResponse;


        }
    }

    /**
     * Имитация обработки тела запроса, для того, чтобы можно было убедится, что оно долетает в целости до ServletDispatcher
     */
    private String workWithBody(HttpRequest request) throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength <= 0) {
            logger.warn("Некорректный Content-Length: " + contentLength);
            return "";
        }

        InputStream inputStream = request.getBodyStream();
        byte[] buffer = new byte[contentLength];
        int totalRead = 0;

        while (totalRead < contentLength) {
            int bytesRead = inputStream.read(buffer, totalRead, contentLength - totalRead);
            if (bytesRead == -1) {
                logger.warn("Достигнут EOF до полной загрузки тела. Прочитано: " + totalRead + "/" + contentLength);
                break;
            }
            totalRead += bytesRead;
        }

        logger.debug("Прочитано байт: " + totalRead + " из " + contentLength);
        return new String(buffer, StandardCharsets.UTF_8);
    }
}





