package ru.otus.professional.yampolskiy.http;

import ru.otus.professional.yampolskiy.http.webserver.ConfigLoader;
import ru.otus.professional.yampolskiy.http.webserver.HttpServer;

public class Application {
    public static void main(String[] args) throws Exception {
        String portProperty = System.getProperty("server.port");
        int port;
        if (portProperty != null) {
            port = Integer.parseInt(portProperty);
        } else {
            port = ConfigLoader.getIntProperty("server.port", 8189);
        }
        new HttpServer(port).start();
    }
}