package ru.otus.crm.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ru.otus.crm.model.Client;

public interface DBServiceClient {

    Client saveClient(Client client);

    Optional<Client> getClient(UUID id);

    List<Client> findAll();

}
