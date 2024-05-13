package ru.otus.mapper;

import org.springframework.stereotype.Component;
import ru.otus.controller.dto.ClientDto;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;

import java.util.Set;

@Component
public class ClientMapper {

    public Client toClient(ClientDto dto) {
        return new Client(dto.name(), new Address(dto.address()), Set.of(new Phone(dto.phone())));
    }

}
