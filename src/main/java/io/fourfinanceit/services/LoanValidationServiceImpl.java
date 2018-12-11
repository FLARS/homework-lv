package io.fourfinanceit.services;

import io.fourfinanceit.domain.Loan;
import io.fourfinanceit.domain.LoanStatus;
import io.fourfinanceit.domain.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalTime;

@Service
@AllArgsConstructor
public class LoanValidationServiceImpl implements LoanValidationService {

    private static final BigDecimal MAX_AMOUNT = BigDecimal.valueOf(1000);
    private static final int MAX_REQUEST_COUNT = 3;

    private final RiskAnalysisService riskAnalysisService;
    private final IpAddressRequestService ipAddressRequestService;
    private final ClockService clockService;

    @Override
    public void validateRisk(Loan loan, HttpServletRequest request) {
        validateTimeAndAmount(loan);
        validateIpAddressRequestCount(loan, request);
    }

    @Override
    public void validateExtension(Loan loan) {
        validateLoanStatus(loan.getStatus());
    }

    private void validateLoanStatus(LoanStatus loanStatus) {
        if (loanStatus == LoanStatus.EXTENDED || loanStatus == LoanStatus.CLOSED) {
            throw new ValidationException("Loan can't be extended, loan is already extended or resolved");
        }
    }

    private void validateTimeAndAmount(Loan loan) {
        if (validateAmount(loan) && validateRiskyTime()) {
            riskAnalysisService.makeAnalysis(loan);
        }
    }

    private void validateIpAddressRequestCount(Loan loan, HttpServletRequest request) {
        if (ipAddressRequestService.getRequestCount(request) >= MAX_REQUEST_COUNT) {
            riskAnalysisService.makeAnalysis(loan);
        }
    }

    private boolean validateAmount(Loan loan) {
        return loan.getAmount().compareTo(MAX_AMOUNT) >= 0;
    }

    private boolean validateRiskyTime() {
        LocalTime loanRequestTime = clockService.getLocalDateNow();
        return loanRequestTime.isAfter(LocalTime.MIN)
                && loanRequestTime.isBefore(LocalTime.of(9,0,0));
    }

}
