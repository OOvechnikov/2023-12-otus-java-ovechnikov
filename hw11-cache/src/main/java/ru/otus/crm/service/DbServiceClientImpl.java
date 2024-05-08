package ru.otus.crm.service;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

@Slf4j
public class DbServiceClientImpl implements DBServiceClient {

    private static final HwCache<Long, Client> CACHE = new MyCache<>();

    private final DataTemplate<Client> dataTemplate;
    private final TransactionRunner transactionRunner;

    public DbServiceClientImpl(TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                var clientId = dataTemplate.insert(connection, client);
                var createdClient = new Client(clientId, client.getName());
                log.info("created client: {}", createdClient);
                CACHE.put(clientId, createdClient);
                return createdClient;
            }
            dataTemplate.update(connection, client);
            log.info("updated client: {}", client);
            CACHE.put(client.getId(), client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        val cachedClient = CACHE.get(id);
        if (cachedClient != null) {
            return Optional.of(cachedClient);
        }
        return transactionRunner.doInTransaction(connection -> {
            var clientOptional = dataTemplate.findById(connection, id);
            log.info("client: {}", clientOptional);
            clientOptional.ifPresent(client -> CACHE.put(client.getId(), client));
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var clientList = dataTemplate.findAll(connection);
            log.info("clientList:{}", clientList);
            clientList.forEach(client -> CACHE.put(client.getId(), client));
            return clientList;
        });
    }
}
