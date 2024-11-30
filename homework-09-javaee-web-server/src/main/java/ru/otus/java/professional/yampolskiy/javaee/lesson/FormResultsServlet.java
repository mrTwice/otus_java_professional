package ru.otus.java.professional.yampolskiy.javaee.lesson;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "FormResultsServlet", urlPatterns = "/formResults.html")
public class FormResultsServlet extends HttpServlet {
    // GET http://localhost:8080/jee/formResults.html?info=Bob
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html");
        String infoParam = req.getParameter("info");
        out.println("<html><body><h1>" + infoParam + "</h1></body></html>");
        out.close();
    }
}
