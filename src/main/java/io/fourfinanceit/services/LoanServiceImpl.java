package io.fourfinanceit.services;

import io.fourfinanceit.domain.Client;
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
    private final ClientService clientService;
    private final LoanRepository repository;

    public List<Loan> findLoansByClientId(Long clientId) {
        List<Loan> clientLoans = repository.findByClientId(clientId);
        if (clientLoans.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return clientLoans;
    }

    public Loan findLoanById(Long loanId) {
        return Optional.ofNullable(repository.findOne(loanId)).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public Long createLoan(Loan loan, HttpServletRequest request) {
        validationService.validateRisk(loan, request);
        Long clientId = loan.getClient().getId();
        Client client = Optional
                .ofNullable(clientService.findClient(clientId))
                .orElseThrow(EntityNotFoundException::new);
        loan.setClient(client);
        initCreationData(loan);
        return repository.save(loan).getId();
    }

    @Transactional
    public Loan extendLoan(Long loanId) {
        Loan existingLoan = Optional
                .ofNullable(repository.findOne(loanId))
                .orElseThrow(EntityNotFoundException::new);
        validationService.validateExtension(existingLoan);

        initExtendData(existingLoan);
        return repository.save(existingLoan);
    }

    private void initCreationData(Loan loan) {
        loan.setUpdatedDate(LocalDateTime.now());
        loan.setCreatedDate(LocalDateTime.now());
        loan.setStatus(LoanStatus.ACTIVE);
        loan.setInterest(DEFAULT_INTEREST);
    }

    private void initExtendData(Loan existingLoan) {
        LocalDate newTermEnd = existingLoan.getTermEnd().plusMonths(1);
        existingLoan.setTermEnd(newTermEnd);
        existingLoan.setUpdatedDate(LocalDateTime.now());
        existingLoan.setStatus(LoanStatus.EXTENDED);
        existingLoan.setInterest(EXTENSION_INTEREST);
    }

}
