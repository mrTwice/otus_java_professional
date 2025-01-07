package ru.otus.professional.yampolskiy.http.webserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.professional.yampolskiy.http.webserver.interfaces.RequestHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HttpServer {
    private final Logger logger = LogManager.getLogger(HttpServer.class);
    private final int port;
    private boolean isRunning = false;
    private ExecutorService threadPool;
    private RequestHandler requestHandler;
    private ServerSocket serverSocket;

    public HttpServer(int port) throws Exception {
        this.port = port;
        this.requestHandler = createRequestHandler();
        this.threadPool = createThreadPool();
    }

    private ExecutorService createThreadPool() {
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    private RequestHandler createRequestHandler() throws Exception {
        return new ServletDispatcher();
    }

    public void start() {
        isRunning = true;
        try {
            serverSocket = new ServerSocket(port);
            logger.debug("[ОТЛАДКА]  Сервер запущен на порту: {}", port);

            while (isRunning) {
                try {
                    Socket socket = serverSocket.accept();
                    if (!isRunning) {
                        break;
                    }
                    threadPool.submit(new ConnectionHandler(socket, requestHandler, this));
                } catch (IOException e) {
                    if (!isRunning) {
                        break;
                    }
                    logger.error("[ОТЛАДКА]  Ошибка при принятии подключения", e);
                }
            }
        } catch (IOException e) {
            logger.error("[ОТЛАДКА]  Ошибка запуска сервера", e);
        } finally {
            stop();
        }
    }

    public void stop() {
        isRunning = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }

            threadPool.shutdownNow();
            try {
                if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                    threadPool.shutdownNow();
                    if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                        logger.error("[ОТЛАДКА]  Пул потоков не остановлен");
                    }
                }
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                logger.warn("[ОТЛАДКА]  Ожидание завершения потоков прервано");
            }
        } catch (IOException e) {
            logger.error("[ОТЛАДКА]  Ошибка при остановке сервера", e);
        }
        logger.debug("[ОТЛАДКА]  Сервер остановлен");
    }
}
