package io.fourfinanceit.services;

import io.fourfinanceit.domain.Client;

public interface ClientService {

    Client findClient(Long clientId);

    Client updateClient(Client client, Long clientId);

    Long saveClient(Client client);

}
