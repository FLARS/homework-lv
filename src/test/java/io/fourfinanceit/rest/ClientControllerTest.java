package io.fourfinanceit.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fourfinanceit.domain.Client;
import io.fourfinanceit.mapping.Mapper;
import io.fourfinanceit.rest.dto.ClientDTO;
import io.fourfinanceit.services.ClientService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static io.fourfinanceit.services.TestUtils.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ClientController.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService service;
    @MockBean
    private Mapper mapper;

    private Client client;
    private ClientDTO clientDTO;

    @Before
    public void setUp() {
        client = setupClient(TEST_NAME, TEST_SECOND_NAME);
        clientDTO = setupClientDTO();
        when(mapper.map(client, ClientDTO.class)).thenReturn(clientDTO);
        when(mapper.map(clientDTO, Client.class)).thenReturn(client);
        when(service.updateClient(client, DEFAULT_ID)).thenReturn(client);
        when(service.findClient(DEFAULT_ID)).thenReturn(client);
    }

    @Test
    public void getClient() throws Exception {
        mockMvc.perform(get("/api/clients/{clientId}", DEFAULT_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(DEFAULT_ID))
                .andExpect(jsonPath("$.firstName").value(TEST_NAME))
                .andExpect(jsonPath("$.secondName").value(TEST_SECOND_NAME));
    }

    @Test
    public void createClient() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        final String requestBody = mapper.writeValueAsString(clientDTO);
        mockMvc.perform(post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateClient() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        final String requestBody = mapper.writeValueAsString(clientDTO);
        mockMvc.perform(put("/api/clients/{clientId}", DEFAULT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(DEFAULT_ID))
                .andExpect(jsonPath("$.firstName").value(TEST_NAME))
                .andExpect(jsonPath("$.secondName").value(TEST_SECOND_NAME));
    }

    private ClientDTO setupClientDTO() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setClientId(client.getId());
        clientDTO.setFirstName(client.getFirstName());
        clientDTO.setSecondName(client.getSecondName());
        return clientDTO;
    }
}