package ru.otus.service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import lombok.val;
import org.springframework.stereotype.Service;
import ru.otus.controller.dto.ClientDto;
import ru.otus.mapper.ClientMapper;
import ru.otus.repository.ClientsRepository;

@RequiredArgsConstructor
@Service
public class ClientsService {

    private final ClientMapper mapper;
    @Delegate
    private final ClientsRepository repository;

    public void save(ClientDto clientDto) {
        val client = mapper.toClient(clientDto);
        repository.save(client);
    }

}
