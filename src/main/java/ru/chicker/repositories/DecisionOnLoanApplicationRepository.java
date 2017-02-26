package ru.chicker.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.chicker.entities.DecisionOnLoanApplication;
import ru.chicker.entities.LoanApplication;

@Repository
public interface DecisionOnLoanApplicationRepository extends
    CrudRepository<DecisionOnLoanApplication, Long> {

    DecisionOnLoanApplication findByLoanApplication(LoanApplication loanApplication);
}
