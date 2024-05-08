package ru.otus.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.controller.dto.ClientDto;
import ru.otus.model.Client;
import ru.otus.service.ClientsService;

import java.util.List;

@RestController
//@RequestMapping("/api/v1/clients")
@RequestMapping
@RequiredArgsConstructor
public class ClientsRestController {

    private final ClientsService clientsService;

    @GetMapping("/api/v1/clients")
    public ResponseEntity<List<Client>> getAllClients() {
        val clients = clientsService.findAll();
        return ResponseEntity.ok(clients);
    }

    @PostMapping(value = "/clients", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> saveClient(ClientDto clientDto) {
        clientsService.save(clientDto);
        return ResponseEntity.ok().build();
    }

}
