package io.fourfinanceit.services;

import io.fourfinanceit.domain.Loan;

public interface RiskAnalysisService {

    void makeAnalysis(Loan loan);

}
