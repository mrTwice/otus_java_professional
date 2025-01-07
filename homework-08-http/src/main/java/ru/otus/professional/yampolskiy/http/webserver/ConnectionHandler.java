package ru.otus.professional.yampolskiy.http.webserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpRequest;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpResponse;
import ru.otus.professional.yampolskiy.http.webserver.interfaces.RequestHandler;
import ru.otus.professional.yampolskiy.http.webserver.parser.HttpParser;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import ru.otus.professional.yampolskiy.http.webserver.http.*;

public class ConnectionHandler implements Runnable {
    private static final Logger logger = LogManager.getLogger(ConnectionHandler.class);
    private final HttpServer server;
    private final Socket socket;
    private final RequestHandler requestHandler;

    public ConnectionHandler(Socket socket, RequestHandler requestHandler, HttpServer server) {
        this.server = server;
        this.socket = socket;
        this.requestHandler = requestHandler;
    }

    @Override
    public void run() {
        try{
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            HttpRequest httpRequest = parseRequest(in);
            if (httpRequest != null) {
                if (isShutdown(httpRequest, out)) return;
                logger.debug("[ОТЛАДКА]  Адрес с нормальным запросом: {}", socket.getRemoteSocketAddress());
                httpRequest.setSocket(socket);
                HttpResponse httpResponse = requestHandler.execute(httpRequest);
                sendResponse(httpResponse, out);
            } else {
                logger.debug("[ОТЛАДКА]  Адрес пустого запроса: {}", socket.getRemoteSocketAddress());
            }


        } catch (Exception e) {
            logger.error("[ОТЛАДКА]  Ошибка обработки запроса", e);
            sendErrorResponse(socket, HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isShutdown(HttpRequest httpRequest, OutputStream out) {
        if (httpRequest.getUri().getPath().equals("/shutdown")) {
            server.stop();
            logger.debug("Сервер будет остановлен");
            sendResponse(new HttpResponse.Builder()
                    .setProtocolVersion(httpRequest.getProtocolVersion())
                    .setStatus(HttpStatus.OK)
                    .setBody("Сервер будет остановлен")
                    .build(), out);
            return true;
        }
        return false;
    }

    private HttpRequest parseRequest(InputStream in) throws IOException {
        HttpRequest httpRequest = new HttpRequest();
        ByteArrayOutputStream headersBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int bytesRead;
        int bodyStartIndex = 0;

        while ((bytesRead = in.read(buffer)) != -1) {
            int headerEndIndex = findHeaderEnd(buffer, bytesRead);
            if (headerEndIndex != -1) {
                headersBuffer.write(buffer, 0, headerEndIndex);
                bodyStartIndex = headerEndIndex;
                logger.debug("[ОТЛАДКА] Индекс конца заголовка: {} ", headerEndIndex);
                break;
            } else {
                headersBuffer.write(buffer, 0, bytesRead);
            }
        }

        if (headersBuffer.size() != 0) {
            HttpParser.parseHeaders(httpRequest, headersBuffer.toString(StandardCharsets.UTF_8));
        } else {
            logger.debug("[ОТЛАДКА] кривой запрос: {}", httpRequest);
            logger.debug("[ОТЛАДКА] Пустые заголовки, возвращаем null.");
            return null;
        }

        InputStream bodyStream = new SequenceInputStream(
                new ByteArrayInputStream(buffer, bodyStartIndex, bytesRead - bodyStartIndex),
                in
        );
        httpRequest.setBodyStream(bodyStream);
        logger.debug("[ОТЛАДКА] Тело начинается с индекса:  {}", bodyStartIndex);
        logger.debug("[ОТЛАДКА] Длина прочитанного до тела: {} байт", headersBuffer.size());

        return httpRequest;
    }


    private int findHeaderEnd(byte[] buffer, int length) {
        for (int i = 0; i < length - 3; i++) {
            if (buffer[i] == '\r' && buffer[i + 1] == '\n' &&
                    buffer[i + 2] == '\r' && buffer[i + 3] == '\n') {
                return i + 4;
            }
        }
        return -1;
    }

    private void sendResponse(HttpResponse httpResponse, OutputStream out) {
        if (socket.isClosed()) {
            logger.debug("[ОТЛАДКА]  Сокет был закрыт до отправки сообщения");
            return;
        }
        try {
            out.write(httpResponse.toString().getBytes(StandardCharsets.UTF_8));
            out.flush();
            logger.debug("[ОТЛАДКА]  Ответ успешно отправлен.");
        } catch (IOException e) {
            logger.error("[ОТЛАДКА]  Ошибка при отправке ответа", e);
        }
    }

    private void sendErrorResponse(Socket socket, HttpStatus httpStatus, String message) {
        try (OutputStream out = socket.getOutputStream()) {
            HttpResponse httpResponse = new HttpResponse.Builder()
                    .setProtocolVersion("HTTP/1.1")
                    .setStatus(httpStatus)
                    .addHeader(HttpHeader.CONTENT_TYPE, "text/plain; charset=UTF-8")
                    .setBody(message)
                    .build();

            out.write(httpResponse.toString().getBytes(StandardCharsets.UTF_8));
            out.flush();
        } catch (IOException e) {
            logger.error("[ОТЛАДКА]  Ошибка при отправке ответа с ошибкой", e);
        }
    }

    private void closeSocket() {
        if (!socket.isClosed()) {
            try {
                socket.close();
                logger.debug("[ОТЛАДКА]  Сокет успешно закрыт.");
            } catch (IOException e) {
                logger.error("[ОТЛАДКА]  Ошибка при закрытии сокета", e);
            }
        }
    }
}



