package io.fourfinanceit.services;

import io.fourfinanceit.domain.Loan;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface LoanService {

    List<Loan> findLoansByClientId(Long clientId);

    Loan findLoanById(Long loanId);

    Long createLoan(Loan loan, HttpServletRequest request);

    Loan extendLoan(Long loanId);

}
