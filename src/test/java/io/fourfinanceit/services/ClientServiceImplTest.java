package io.fourfinanceit.services;

import io.fourfinanceit.domain.Client;
import io.fourfinanceit.domain.exceptions.EntityNotFoundException;
import io.fourfinanceit.persistence.ClientRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static io.fourfinanceit.services.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Client client;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(clientService);
        client = setupClient(TEST_NAME, TEST_SECOND_NAME);
        when(clientRepository.save(client)).thenReturn(client);
        when(clientRepository.findOne(client.getId())).thenReturn(client);
    }

    @Test
    public void testSaveClient() {
        Long clientId = clientService.saveClient(client);
        assertNotNull(clientId);
        assertEquals(DEFAULT_ID, clientId);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateInvalidId() {
        clientService.updateClient(client, INVALID_ID);
    }

    @Test
    public void testUpdate() {
        Client testClient = setupClient(INVALID_NAME, INVALID_SECOND_NAME);
        when(clientRepository.save(testClient)).thenReturn(testClient);
        Client updateClient = clientService.updateClient(testClient, DEFAULT_ID);

        assertNotNull(updateClient);
        assertEquals(DEFAULT_ID, updateClient.getId());
        assertEquals(INVALID_NAME, updateClient.getFirstName());
        assertEquals(INVALID_SECOND_NAME, updateClient.getSecondName());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testFindInvalidId() {
        clientService.findClient(INVALID_ID);
    }

    @Test()
    public void testFind() {
        Client client = clientService.findClient(DEFAULT_ID);

        assertNotNull(client);
        assertEquals(DEFAULT_ID, client.getId());
        assertEquals(TEST_NAME, client.getFirstName());
        assertEquals(TEST_SECOND_NAME, client.getSecondName());
    }

}