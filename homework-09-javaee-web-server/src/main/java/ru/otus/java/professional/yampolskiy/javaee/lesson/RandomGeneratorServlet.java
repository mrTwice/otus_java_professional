package ru.otus.java.professional.yampolskiy.javaee.lesson;


import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(name = "CalcServlet", urlPatterns = "/calc")
public class RandomGeneratorServlet extends HttpServlet {
    private static Logger logger = LogManager.getLogger(RandomGeneratorServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html");
        int min = Integer.parseInt(req.getParameter("min"));
        int max = Integer.parseInt(req.getParameter("max"));
        String result = String.valueOf(min + (int)(Math.random() * (max - min)));
        out.println("<html><body><h1>" + result + "</h1></body></html>");
        out.close();
    }
}
