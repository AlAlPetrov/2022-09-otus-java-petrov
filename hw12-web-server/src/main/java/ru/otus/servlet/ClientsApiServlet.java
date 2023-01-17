package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.model.Client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;


public class ClientsApiServlet extends HttpServlet {

    private final Gson gson;
    private final DBServiceClient clientService;

    public ClientsApiServlet(DBServiceClient clientService, Gson gson) {
        this.clientService = clientService;
        this.gson = gson;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var inputStream = request.getInputStream();
        var client = gson.fromJson(new InputStreamReader(inputStream), Client.class);
        clientService.saveClient(client);
        response.setContentType("text/html");
        response.sendRedirect("/clients");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        var clients = clientService.findAll();

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(gson.toJson(clients));
        out.flush();
    }
}