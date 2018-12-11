package io.fourfinanceit.services;

import io.fourfinanceit.domain.Loan;

import javax.servlet.http.HttpServletRequest;

public interface LoanValidationService {

    void validateRisk(Loan loan, HttpServletRequest request);

    void validateExtension(Loan loan);

}
