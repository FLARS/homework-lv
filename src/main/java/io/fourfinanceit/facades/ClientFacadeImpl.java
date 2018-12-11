package io.fourfinanceit.facades;

import io.fourfinanceit.domain.Client;
import io.fourfinanceit.services.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ClientFacadeImpl implements ClientFacade {

    private final ClientService service;

    @Override
    public Client findClient(Long clientId) {
        return service.findClient(clientId);
    }

    @Override
    public Long saveClient(Client client) {
        return service.saveClient(client);
    }

    @Override
    public Client updateClient(Client client, Long clientId) {
        return service.updateClient(client, clientId);
    }
}
