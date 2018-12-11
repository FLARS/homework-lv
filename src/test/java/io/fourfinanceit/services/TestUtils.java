package io.fourfinanceit.services;

import io.fourfinanceit.domain.Client;
import io.fourfinanceit.domain.Loan;
import io.fourfinanceit.domain.LoanStatus;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    public static final Long DEFAULT_ID = 1L;
    public static final String TEST_NAME = "TestName";
    public static final String TEST_SECOND_NAME = "TestSecondName";
    public static final String CURRENCY_USD = "USD";
    static final String INVALID_NAME = "InvalidName";
    static final String INVALID_SECOND_NAME = "InvalidSecondName";
    static final Long INVALID_ID = 2L;

    public static Client setupClient(String name, String secondName) {
        Client client = new Client();
        client.setId(DEFAULT_ID);
        client.setFirstName(name);
        client.setSecondName(secondName);
        return client;
    }

    public static Loan setupLoan() {
        Loan loan = new Loan();
        loan.setId(DEFAULT_ID);
        loan.setAmount(BigDecimal.valueOf(1000));
        loan.setCurrency("LVL");
        loan.setInterest(1.0f);
        loan.setStatus(LoanStatus.ACTIVE);
        loan.setClient(setupClient(TEST_NAME, TEST_SECOND_NAME));
        loan.setTermStart(LocalDate.now());
        loan.setTermEnd(LocalDate.now());
        return loan;
    }

    public static List<Loan> setupClientLoans() {
        List<Loan> loans = new ArrayList<>();
        loans.add(setupLoan());
        loans.add(setupLoan());
        loans.add(setupLoan());
        return loans;
    }

    public static HttpServletRequest setupRequest() {
        return new MockHttpServletRequest();
    }
}
