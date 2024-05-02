package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.val;
import ru.otus.services.DBServiceClient;

import java.io.IOException;

@SuppressWarnings({"squid:S1948"})
@RequiredArgsConstructor
public class ClientsApiServlet extends HttpServlet {

    private final DBServiceClient dbServiceClient;
    private final Gson gson;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        val clients = dbServiceClient.findAll();

        response.setContentType("application/json;charset=UTF-8");
        response.getOutputStream().print(gson.toJson(clients));
    }

}
