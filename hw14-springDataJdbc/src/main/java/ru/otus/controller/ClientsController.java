package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.controller.dto.ClientDto;
import ru.otus.service.ClientsService;

@Controller
@RequiredArgsConstructor
public class ClientsController {

    private final ClientsService clientsService;

    @GetMapping(path = {"/", "/clients"})
    public String getClientPage() {
        return "clients";
    }

}
