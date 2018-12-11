package io.fourfinanceit.rest;


import io.fourfinanceit.domain.Client;
import io.fourfinanceit.mapping.Mapper;
import io.fourfinanceit.rest.dto.ClientDTO;
import io.fourfinanceit.services.ClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@AllArgsConstructor
@RequestMapping(value = "api/clients", produces = {MediaType.APPLICATION_JSON_VALUE})
@Api(tags = {"clients"}, description = "Clients API")
public class ClientController {

    private final ClientService service;
    private final Mapper mapper;

    @GetMapping(value = "/{clientId}")
    @ApiOperation(value = "Get client by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Client retrieved"),
            @ApiResponse(code = 400, message = "Invalid client id has been provided"),
            @ApiResponse(code = 404, message = "Client not found")
    })
    public ResponseEntity<ClientDTO> getClient(@PathVariable Long clientId) {
        Client client = service.findClient(clientId);
        ClientDTO clientDTO = mapper.map(client, ClientDTO.class);
        return new ResponseEntity<>(clientDTO, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Client created"),
            @ApiResponse(code = 400, message = "Invalid client body provided"),
    })
    public ResponseEntity createClient(@RequestBody ClientDTO clientDTO) {
        Client mappedFromDTO = mapper.map(clientDTO, Client.class);
        Long clientId = service.saveClient(mappedFromDTO);

        Link clientLocation = ControllerLinkBuilder
                .linkTo(methodOn(ClientController.class).getClient(clientId))
                .withSelfRel()
                .expand();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, clientLocation.getHref());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{clientId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Client updated"),
            @ApiResponse(code = 400, message = "Invalid client body provided"),
    })
    public ResponseEntity<ClientDTO> updateClient(@RequestBody ClientDTO clientDTO, @PathVariable Long clientId) {
        Client mappedFromDTO = mapper.map(clientDTO, Client.class);
        Client client = service.updateClient(mappedFromDTO, clientId);
        ClientDTO updateClient = mapper.map(client, ClientDTO.class);

        Link loanLocation = ControllerLinkBuilder
                .linkTo(methodOn(ClientController.class).getClient(client.getId()))
                .withSelfRel()
                .expand();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, loanLocation.getHref());
        return new ResponseEntity<>(updateClient, headers, HttpStatus.OK);
    }

}
