package ru.otus.java.professional.yampolskiy.javaee;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.logging.Logger;

@WebFilter(urlPatterns = {"/add", "/subtract", "/multiply", "/div"})
public class LoggingFilter implements Filter {
    private static final Logger logger = Logger.getLogger(LoggingFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        logger.info("Входящий запрос " + httpRequest.getMethod() + " " + httpRequest.getRequestURI() +
                " с параметрами " + httpRequest.getQueryString());
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
