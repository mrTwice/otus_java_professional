package ru.otus.professional.yampolskiy.http.webserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpHeader;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpRequest;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpResponse;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpStatus;
import ru.otus.professional.yampolskiy.http.webserver.interfaces.RequestHandler;
import ru.otus.professional.yampolskiy.http.webserver.parser.HttpParser;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

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
        try {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            HttpRequest httpRequest = parseRequest(in);
            if(httpRequest == null) {
                System.out.println("Пустой запрос");
            } else {
                if (isShutdown(httpRequest, out)) {
                    return;
                }
                httpRequest.setSocket(socket);
                HttpResponse httpResponse = requestHandler.execute(httpRequest);
                sendResponse(httpResponse, out);
            }
        } catch (Exception e) {
            sendErrorResponse(socket, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            closeSocket();
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
        BufferedInputStream bufferedInput = new BufferedInputStream(in);
        PushbackInputStream pushbackInput = new PushbackInputStream(bufferedInput, 8192);

        byte[] buffer = new byte[8192];
        int totalBytesRead = 0, bytesRead;
        int headersEndIndex = -1;

        while ((bytesRead = pushbackInput.read(buffer)) != -1) {
            totalBytesRead += bytesRead;
            headersEndIndex = findHeadersEnd(buffer, bytesRead);
            if (headersEndIndex != -1) {
                parseRequest(httpRequest, new String(buffer, 0, headersEndIndex, StandardCharsets.UTF_8));

                int remaining = bytesRead - headersEndIndex;
                if (remaining > 0) {
                    pushbackInput.unread(buffer, headersEndIndex, remaining);
                }
                if (pushbackInput.available() > 0) {
                    httpRequest.setBodyStream(pushbackInput);
                }
                return httpRequest;
            }
            if (totalBytesRead > 64 * 1024) {
                throw new IOException("Превышен максимальный размер заголовков (64 KB)");
            }
        }
        return null;
    }


    private void parseRequest(HttpRequest httpRequest, String request) {
        HttpParser.parse(httpRequest, request);
    }

    private int findHeadersEnd(byte[] buffer, int length) {
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
            logger.error("[ОТЛАДКА]  Ошибка при отправке ответа, сокет закрыт");
            return;
        }
        try {
            out.write(httpResponse.toString().getBytes(StandardCharsets.UTF_8));
            out.flush();
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
            } catch (IOException e) {
                logger.error("[ОТЛАДКА]  Ошибка при закрытии сокета", e);
            }
        }
    }
}



