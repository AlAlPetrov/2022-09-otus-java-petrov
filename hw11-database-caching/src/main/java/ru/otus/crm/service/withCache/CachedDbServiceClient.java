package ru.otus.crm.service.withCache;

import ru.otus.cachehw.HwCache;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;

import java.util.List;
import java.util.Optional;

public class CachedDbServiceClient implements DBServiceClient {
    private final DBServiceClient dbServiceClient;
    private final HwCache<Long, Client> hwCache;

    public CachedDbServiceClient(DBServiceClient dbServiceClient, HwCache<Long, Client> hwCache) {
        this.dbServiceClient = dbServiceClient;
        this.hwCache = hwCache;
    }

    @Override
    public Client saveClient(Client client) {
        var savedClient = dbServiceClient.saveClient(client);
        hwCache.put(savedClient.getId(), savedClient);

        return savedClient;
    }

    @Override
    public Optional<Client> getClient(long id) {
        var client = hwCache.get(id);
        if (client != null) {
            return Optional.of(client);
        }

        client = dbServiceClient.getClient(id).get();
        if (client == null) {
            return Optional.empty();
        }

        hwCache.put(id, client);
        return Optional.of(client);
    }

    @Override
    public List<Client> findAll() {
        return dbServiceClient.findAll();
    }
}
