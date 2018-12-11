package io.fourfinanceit.persistence;

import io.fourfinanceit.domain.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    @Query("SELECT loan FROM Loan loan WHERE loan.client.id = ?1")
    List<Loan> findByClientId(Long clientId);
}
