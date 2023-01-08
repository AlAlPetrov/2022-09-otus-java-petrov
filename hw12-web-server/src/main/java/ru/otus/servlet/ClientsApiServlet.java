package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;

import java.io.IOException;
import java.io.InputStreamReader;


public class ClientsApiServlet extends HttpServlet {

    private final Gson gson;
    private final DBServiceClient clientService;

    public ClientsApiServlet(DBServiceClient clientService, Gson gson) {
        this.clientService = clientService;
        this.gson = gson;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        var inputStream = request.getInputStream();
        var client = gson.fromJson(new InputStreamReader(inputStream), Client.class);
        clientService.saveClient(client);

        //clientService.saveClient(new Client(null,
        //      "test client name 2",
        //    new Address(null, "test street 2"),
        //  List.of(new Phone(null, "222"), new Phone(null, "333"))));
        response.sendRedirect("/clients");
    }
}