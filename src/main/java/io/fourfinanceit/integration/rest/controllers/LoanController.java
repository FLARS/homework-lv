package io.fourfinanceit.integration.rest.controllers;


import io.fourfinanceit.domain.Loan;
import io.fourfinanceit.facades.LoanFacade;
import io.fourfinanceit.integration.dto.LoanDTO;
import io.fourfinanceit.mapping.Mapper;
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

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@AllArgsConstructor
@RequestMapping(value = "api/loans", produces = {MediaType.APPLICATION_JSON_VALUE})
@Api(tags = {"loans"}, description = "Loans API")
public class LoanController {

    private final LoanFacade facade;
    private final Mapper mapper;

    @GetMapping(value = "/clients/{clientId}")
    @ApiOperation(value = "Get all loans(incl. extended ones) by client Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Client loans retrieved"),
            @ApiResponse(code = 400, message = "Invalid client id has been provided"),
            @ApiResponse(code = 404, message = "There are no loans found for requested client")
    })
    public ResponseEntity<List<LoanDTO>> getClientLoans(@PathVariable Long clientId) {
        List<Loan> clientLoans = facade.findLoansByClientId(clientId);
        List<LoanDTO> loanDTOS = mapper.mapList(clientLoans, LoanDTO.class);
        return new ResponseEntity<>(loanDTOS, HttpStatus.OK);
    }

    @GetMapping(value = "/{loanId}")
    @ApiOperation(value = "Get loan by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Loan is retrieved"),
            @ApiResponse(code = 400, message = "Invalid loan id has been provided"),
            @ApiResponse(code = 404, message = "Loan has not been found")
    })
    public ResponseEntity<LoanDTO> getLoanById(@PathVariable Long loanId) {
        Loan loan = facade.findLoanById(loanId);
        LoanDTO loanDTO = mapper.map(loan, LoanDTO.class);
        return new ResponseEntity<>(loanDTO, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Register loan in the system")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Loan has been saved"),
            @ApiResponse(code = 400, message = "Invalid loan has been submitted")
    })
    public ResponseEntity registerLoan(@RequestBody LoanDTO loanDTO, HttpServletRequest request) {
        Loan loan = mapper.map(loanDTO, Loan.class);
        Long loanId = facade.registerLoan(loan, request);

        Link loanLocation = ControllerLinkBuilder
                .linkTo(methodOn(LoanController.class).getLoanById(loanId))
                .withSelfRel()
                .expand();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, loanLocation.getHref());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{loanId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Extend existing loan")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Loan has been extended"),
            @ApiResponse(code = 400, message = "Invalid loan id has been provided"),
            @ApiResponse(code = 404, message = "There is no loans found for requested client")
    })
    public ResponseEntity<LoanDTO> extendLoan(@PathVariable Long loanId) {
        Loan extendedLoan = facade.extendLoan(loanId);
        LoanDTO loanDTO = mapper.map(extendedLoan, LoanDTO.class);
        return new ResponseEntity<>(loanDTO, HttpStatus.OK);
    }

}
