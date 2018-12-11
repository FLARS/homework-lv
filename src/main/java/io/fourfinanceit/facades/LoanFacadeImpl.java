package io.fourfinanceit.facades;

import io.fourfinanceit.domain.Client;
import io.fourfinanceit.domain.Loan;
import io.fourfinanceit.services.ClientService;
import io.fourfinanceit.services.IpAddressRequestService;
import io.fourfinanceit.services.LoanService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
@AllArgsConstructor
public class LoanFacadeImpl implements LoanFacade {

    private final LoanService loanService;
    private final ClientService clientService;
    private final IpAddressRequestService ipAddressRequestService;

    public Long registerLoan(Loan loan, HttpServletRequest request) {
        ipAddressRequestService.addIpRequest(request);
        Client client = clientService.findClient(loan.getClient().getId());
        loan.setClient(client);
        return loanService.createLoan(loan, request);
    }

    public Loan extendLoan(Long loanId) {
        return loanService.extendLoan(loanId);
    }

    public Loan findLoanById(Long loanId) {
        return loanService.findById(loanId);
    }

    public List<Loan> findLoansByClientId(Long clientId) {
        return loanService.findLoansByClientId(clientId);
    }


}
