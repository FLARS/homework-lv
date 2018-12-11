package io.fourfinanceit.integration.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fourfinanceit.domain.Client;
import io.fourfinanceit.domain.Loan;
import io.fourfinanceit.facades.ClientFacade;
import io.fourfinanceit.facades.LoanFacade;
import io.fourfinanceit.integration.dto.ClientDTO;
import io.fourfinanceit.integration.dto.LoanDTO;
import io.fourfinanceit.mapping.Mapper;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static io.fourfinanceit.services.TestUtils.*;
import static io.fourfinanceit.services.TestUtils.DEFAULT_ID;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(LoanController.class)
public class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanFacade facade;
    @MockBean
    private Mapper mapper;

    private LoanDTO loanDTO;

    @Before
    public void setUp() {
        Loan loan = setupLoan();
        loanDTO = setupLoanDTO();
        List<Loan> clientLoans = setupClientLoans();
        when(mapper.map(loan, LoanDTO.class)).thenReturn(loanDTO);
        when(mapper.map(loanDTO, Loan.class)).thenReturn(loan);
        when(mapper.mapList(clientLoans, LoanDTO.class)).thenReturn(Arrays.asList(loanDTO, loanDTO, loanDTO));
        when(facade.extendLoan(DEFAULT_ID)).thenReturn(loan);
        when(facade.findLoanById(DEFAULT_ID)).thenReturn(loan);
        when(facade.registerLoan(loan, setupRequest())).thenReturn(DEFAULT_ID);
        when(facade.findLoansByClientId(DEFAULT_ID)).thenReturn(clientLoans);
    }

    @Test
    public void getClientLoans() throws Exception {
        mockMvc.perform(get("/api/loans/clients/{clientId}", DEFAULT_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].clientId").value(DEFAULT_ID))
                .andExpect(jsonPath("$.[0].amount").value(BigDecimal.ONE))
                .andExpect(jsonPath("$.[0].currency").value(CURRENCY_USD))
                .andExpect(jsonPath("$.[1].clientId").value(DEFAULT_ID))
                .andExpect(jsonPath("$.[1].amount").value(BigDecimal.ONE))
                .andExpect(jsonPath("$.[1].currency").value(CURRENCY_USD))
                .andExpect(jsonPath("$.[2].clientId").value(DEFAULT_ID))
                .andExpect(jsonPath("$.[2].amount").value(BigDecimal.ONE))
                .andExpect(jsonPath("$.[2].currency").value(CURRENCY_USD));
    }

    @Test
    public void getLoanById() throws Exception {
        mockMvc.perform(get("/api/loans/{loanId}", DEFAULT_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(DEFAULT_ID))
                .andExpect(jsonPath("$.amount").value(BigDecimal.ONE))
                .andExpect(jsonPath("$.currency").value(CURRENCY_USD));
    }

    @Test
    public void registerLoan() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(loanDTO);
        mockMvc.perform(post("/api/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    public void extendLoan() throws Exception {
        mockMvc.perform(put("/api/loans/{loanId}", DEFAULT_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(DEFAULT_ID))
                .andExpect(jsonPath("$.amount").value(BigDecimal.ONE))
                .andExpect(jsonPath("$.currency").value(CURRENCY_USD));
    }

    private LoanDTO setupLoanDTO() {
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setAmount(BigDecimal.ONE);
        loanDTO.setClientId(DEFAULT_ID);
        loanDTO.setCurrency(CURRENCY_USD);
        loanDTO.setTermEnd(LocalDate.now());
        loanDTO.setTermStart(LocalDate.now());
        return loanDTO;
    }
}