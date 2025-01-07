package ru.otus.professional.yampolskiy.http.webserver.interfaces;

import ru.otus.professional.yampolskiy.http.webserver.http.HttpRequest;
import ru.otus.professional.yampolskiy.http.webserver.http.HttpResponse;

public interface RequestHandler {
    HttpResponse execute(HttpRequest request) throws Exception, Throwable;
}
