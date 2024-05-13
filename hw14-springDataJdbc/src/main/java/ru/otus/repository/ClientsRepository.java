package ru.otus.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.model.Client;

import java.util.UUID;

@Repository
public interface ClientsRepository extends ListCrudRepository<Client, UUID> {
}
