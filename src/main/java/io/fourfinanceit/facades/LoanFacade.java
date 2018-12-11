package io.fourfinanceit.facades;

import io.fourfinanceit.domain.Loan;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface LoanFacade {

    Long registerLoan(Loan loan, HttpServletRequest request);

    Loan extendLoan(Long loanId);

    Loan findLoanById(Long loanId);

    List<Loan> findLoansByClientId(Long clientId);

}
