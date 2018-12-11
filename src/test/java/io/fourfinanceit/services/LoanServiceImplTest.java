package io.fourfinanceit.services;

import io.fourfinanceit.domain.Loan;
import io.fourfinanceit.domain.exceptions.EntityNotFoundException;
import io.fourfinanceit.persistence.LoanRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static io.fourfinanceit.services.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoanServiceImplTest {

    private static final int EXPECTED_SIZE = 3;

    @Mock
    private LoanValidationService validationService;
    @Mock
    private LoanRepository repository;

    @InjectMocks
    private LoanServiceImpl loanService;

    private Loan loan;
    private HttpServletRequest request;

    @Before
    public void setUp() {
        loan = setupLoan();
        request = setupRequest();
        when(repository.findOne(DEFAULT_ID)).thenReturn(loan);
        when(repository.findByClientId(DEFAULT_ID)).thenReturn(setupClientLoans());
        when(repository.save(loan)).thenReturn(loan);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testInvalidClientId() {
        loanService.findLoansByClientId(INVALID_ID);
    }

    @Test
    public void testFindLoansByClientId() {
        List<Loan> clientLoans = loanService.findLoansByClientId(DEFAULT_ID);

        assertFalse(clientLoans.isEmpty());
        assertEquals(EXPECTED_SIZE, clientLoans.size());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testInvalidLoanId() {
        loanService.findById(INVALID_ID);
    }

    @Test
    public void testFindByLoanId() {
        Loan loan = loanService.findById(DEFAULT_ID);

        assertNotNull(loan);
    }

    @Test
    public void createLoan() {
        loanService.createLoan(loan, request);
        verify(validationService).validateRisk(loan, request);
        verify(repository).save(loan);
    }

    @Test
    public void extendLoan() {
        loanService.extendLoan(DEFAULT_ID);
        verify(repository).findOne(DEFAULT_ID);
        verify(validationService).validateExtension(loan);
        verify(repository).save(loan);
    }

    private List<Loan> setupClientLoans() {
        List<Loan> loans = new ArrayList<>();
        loans.add(setupLoan());
        loans.add(setupLoan());
        loans.add(setupLoan());
        return loans;
    }
}