package io.fourfinanceit.services;

import io.fourfinanceit.domain.Loan;
import io.fourfinanceit.domain.exceptions.ValidationException;
import org.springframework.stereotype.Service;

@Service
public class RiskAnalysisServiceImpl implements RiskAnalysisService {

    @Override
    public void makeAnalysis(Loan loan) {
        // analysis...
        throw new ValidationException("Loan is too risky");
    }

}
