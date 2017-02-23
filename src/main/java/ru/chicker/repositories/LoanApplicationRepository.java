package ru.chicker.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.chicker.entities.LoanApplication;

@Repository
public interface LoanApplicationRepository extends CrudRepository<LoanApplication, Long> {
    LoanApplication findByPersonalId(String personalId);
}
