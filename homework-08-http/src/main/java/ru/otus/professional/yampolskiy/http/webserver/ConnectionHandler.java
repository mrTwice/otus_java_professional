package ru.otus.professional.yampolskiy.http.webserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpStatus;
import ru.otus.professional.yampolskiy.http.webserver.parser.HttpParser;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpRequest;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpResponse;
import ru.otus.professional.yampolskiy.http.webserver.interfaces.RequestHandler;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ConnectionHandler implements Runnable {
    private final Logger logger = LogManager.getLogger(ConnectionHandler.class);
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
        try (InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream()) {

            HttpRequest httpRequest = null;

            try {
                httpRequest = HttpParser.parse(in, socket);

                if (httpRequest == null) {
                    logger.warn("Получен пустой запрос");
                    sendErrorResponse(out, 400, "Bad Request: Empty request");
                    return;
                }

                if (httpRequest.getUri().getPath().equals("/shutdown")) {
                    server.stop();
                    logger.debug("Сервер будет остановлен");
                    sendResponse(new HttpResponse.Builder()
                            .setProtocolVersion(httpRequest.getProtocolVersion())
                            .setStatus(HttpStatus.OK)
                            .setBody("Сервер будет остановлен")
                            .build(), out);
                    return;
                }


                HttpResponse httpResponse = requestHandler.execute(httpRequest);
                sendResponse(httpResponse, out);

            } catch (Exception e) {
                logger.error("Ошибка при обработке запроса", e);
                sendErrorResponse(out, 500, "Internal Server Error");
            }

        } catch (IOException e) {
            logger.error("Ошибка при работе с сокетом", e);
        } finally {
            closeSocket();
        }
    }

    private void sendResponse(HttpResponse httpResponse, OutputStream out) throws IOException {
        try {
            out.write(httpResponse.toString().getBytes(StandardCharsets.UTF_8));
            out.flush();
            logger.debug("Ответ успешно отправлен.");
        } catch (IOException e) {
            logger.error("Ошибка при отправке ответа", e);
            throw e;
        }
    }

    private void closeSocket() {
        if (!socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                logger.error("Ошибка при закрытии сокета", e);
            }
        }
    }

    private void sendErrorResponse(OutputStream out, int statusCode, String message) {
        try {
            String response = "HTTP/1.1 " + statusCode + " " + getReasonPhrase(statusCode) + "\r\n" +
                    "Content-Type: text/plain; charset=UTF-8\r\n" +
                    "Content-Length: " + message.getBytes(StandardCharsets.UTF_8).length + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" +
                    message;

            out.write(response.getBytes(StandardCharsets.UTF_8));
            out.flush();
        } catch (IOException e) {
            logger.error("Ошибка при отправке ответа с ошибкой: {}", e.getMessage());
        }
    }

    private String getReasonPhrase(int statusCode) {
        return switch (statusCode) {
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 500 -> "Internal Server Error";
            case 503 -> "Service Unavailable";
            default -> "Unknown Status";
        };
    }
}


