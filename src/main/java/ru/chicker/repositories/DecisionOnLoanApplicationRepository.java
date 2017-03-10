package ru.chicker.repositories;

import org.springframework.stereotype.Repository;
import ru.chicker.domain.entities.DecisionOnLoanApplication;
import ru.chicker.domain.entities.LoanApplication;

import java.util.List;

@Repository
public interface DecisionOnLoanApplicationRepository extends
    BaseRepository<DecisionOnLoanApplication, Long> {

    DecisionOnLoanApplication findByLoanApplication(LoanApplication loanApplication);
    List<DecisionOnLoanApplication> findByApproved(int approved);
}
