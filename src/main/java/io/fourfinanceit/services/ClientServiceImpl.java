package io.fourfinanceit.services;

import io.fourfinanceit.domain.Client;
import io.fourfinanceit.domain.exceptions.EntityNotFoundException;
import io.fourfinanceit.persistence.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;

    public Long saveClient(Client client) {
        return repository.save(client).getId();
    }

    @Transactional
    public Client updateClient(Client client, Long clientId) {
        Client findClient = Optional.ofNullable(repository.findOne(clientId)).orElseThrow(EntityNotFoundException::new);
        client.setId(findClient.getId());
        return repository.save(client);
    }

    public Client findClient(Long clientId) {
        return Optional.ofNullable(repository.findOne(clientId)).orElseThrow(EntityNotFoundException::new);
    }
}
