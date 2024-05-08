package ru.otus.repository.listener;

import lombok.val;
import org.springframework.data.relational.core.mapping.event.AbstractRelationalEventListener;
import org.springframework.data.relational.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import ru.otus.model.Client;

import java.util.UUID;

@Component
public class ClientListener extends AbstractRelationalEventListener<Client> {

    @Override
    protected void onBeforeConvert(BeforeConvertEvent<Client> event) {
        val client = event.getEntity();
        val clientId = UUID.randomUUID();
        client.setId(clientId);
        client.getAddress().setClientId(clientId);
        client.getPhones().forEach(phone -> phone.setClientId(clientId));
    }

}
