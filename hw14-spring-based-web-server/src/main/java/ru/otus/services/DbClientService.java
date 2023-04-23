package ru.otus.services;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Service;
import ru.otus.domain.Client;
import ru.otus.repository.ClientRepository;
import ru.otus.sessionmanager.TransactionManager;

import java.util.List;

@Service
public class DbClientService implements ClientService{
    private final TransactionManager transactionManager;
    private final ClientRepository clientRepository;

    public DbClientService(TransactionManager transactionManager, ClientRepository clientRepository) {
        this.transactionManager = transactionManager;
        this.clientRepository = clientRepository;
    }
    @Override
    public List<Client> findAll() {
        return IterableUtils.toList(clientRepository.findAll());
    }

    @Override
    public Client save(Client client) {
        return transactionManager.doInTransaction(() -> clientRepository.save(client));
    }
}
