package ru.otus.java.professional.yampolskiy.javaee;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/add", "/subtract", "/multiply", "/div"})
public class CalculatorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");

        String operation = req.getServletPath();
        try {
            double a = parseParameter(req, "a");
            double b = parseParameter(req, "b");
            double result;

            String operationName;
            switch (operation) {
                case "/add":
                    result = a + b;
                    operationName = "Сложение";
                    break;
                case "/subtract":
                    result = a - b;
                    operationName = "Вычитание";
                    break;
                case "/multiply":
                    result = a * b;
                    operationName = "Умножение";
                    break;
                case "/div":
                    if (b == 0) {
                        throw new IllegalArgumentException("Деление на ноль невозможно.");
                    }
                    result = a / b;
                    operationName = "Деление";
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Неизвестная операция.");
                    return;
            }

            String htmlResponse = generateHtmlResponse(operationName, a, b, result);
            resp.getWriter().write(htmlResponse);

        } catch (IllegalArgumentException e) {
            resp.getWriter().write(generateErrorHtml(e.getMessage()));
        }
    }

    /**
     * Метод для извлечения и преобразования параметра из строки в число.
     */
    private double parseParameter(HttpServletRequest req, String paramName) {
        String paramValue = req.getParameter(paramName);
        if (paramValue == null) {
            throw new IllegalArgumentException("Отсутствует обязательный параметр: " + paramName);
        }
        try {
            return Double.parseDouble(paramValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Параметр '" + paramName + "' должен быть числом.");
        }
    }

    /**
     * Генерация HTML-ответа для успешной операции.
     */
    private String generateHtmlResponse(String operationName, double a, double b, double result) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head><title>Результат вычисления</title></head>" +
                "<body>" +
                "<h1>Результат операции</h1>" +
                "<p>Операция: <strong>" + operationName + "</strong></p>" +
                "<p>Операнд A: <strong>" + a + "</strong></p>" +
                "<p>Операнд B: <strong>" + b + "</strong></p>" +
                "<p>Результат: <strong>" + result + "</strong></p>" +
                "</body>" +
                "</html>";
    }

    /**
     * Генерация HTML-ответа для ошибок.
     */
    private String generateErrorHtml(String errorMessage) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head><title>Ошибка</title></head>" +
                "<body>" +
                "<h1>Ошибка</h1>" +
                "<p>" + errorMessage + "</p>" +
                "</body>" +
                "</html>";
    }
}

