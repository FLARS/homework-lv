package io.fourfinanceit.facades;

import io.fourfinanceit.domain.Client;

public interface ClientFacade {

    Client findClient(Long clientId);

    Long saveClient(Client client);

    Client updateClient(Client client, Long clientId);

}
