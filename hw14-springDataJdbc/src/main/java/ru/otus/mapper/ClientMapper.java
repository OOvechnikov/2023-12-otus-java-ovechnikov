package ru.otus.mapper;

import lombok.val;
import org.springframework.stereotype.Component;
import ru.otus.controller.dto.ClientDto;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;

import java.util.Set;
import java.util.UUID;

@Component
public class ClientMapper {

    public Client toClient(ClientDto dto) {
        val clientId = UUID.randomUUID();
        return new Client(clientId, dto.name(), new Address(clientId, dto.address()), Set.of(new Phone(clientId, dto.phone())), true);
//        return Client.builder()
//                .name(dto.name())
//                .address(new Address(dto.address()))
//                .phones(Set.of(new Phone(dto.phone())))
//                .build();
    }

}
