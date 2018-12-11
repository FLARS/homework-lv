package io.fourfinanceit.services;

import io.fourfinanceit.domain.Loan;
import io.fourfinanceit.domain.LoanStatus;
import io.fourfinanceit.domain.exceptions.EntityNotFoundException;
import io.fourfinanceit.persistence.LoanRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LoanServiceImpl implements LoanService {

    private static final float EXTENSION_INTEREST = 1.5f;
    private static final float DEFAULT_INTEREST = 1.0f;

    private final LoanValidationService validationService;
    private final LoanRepository repository;

    public List<Loan> findLoansByClientId(Long clientId) {
        List<Loan> clientLoans = repository.findByClientId(clientId);
        if (clientLoans.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return clientLoans;
    }

    public Loan findById(Long loanId) {
        return Optional.ofNullable(repository.findOne(loanId)).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public Long createLoan(Loan loan, HttpServletRequest request) {
        validationService.validateRisk(loan, request);
        initCreationData(loan);
        Loan savedLoan = repository.save(loan);
        return savedLoan.getId();
    }

    @Transactional
    public Loan extendLoan(Long loanId) {
        Loan existingLoan = findById(loanId);
        validationService.validateExtension(existingLoan);
        initExtensionData(existingLoan);
        return repository.save(existingLoan);
    }

    private void initCreationData(Loan loan) {
        loan.setUpdatedDate(LocalDateTime.now());
        loan.setCreatedDate(LocalDateTime.now());
        loan.setStatus(LoanStatus.ACTIVE);
        loan.setInterest(DEFAULT_INTEREST);
    }

    private void initExtensionData(Loan existingLoan) {
        LocalDate newTermEnd = existingLoan.getTermEnd().plusMonths(1);
        existingLoan.setTermEnd(newTermEnd);
        existingLoan.setUpdatedDate(LocalDateTime.now());
        existingLoan.setStatus(LoanStatus.EXTENDED);
        existingLoan.setInterest(EXTENSION_INTEREST);
    }

}
