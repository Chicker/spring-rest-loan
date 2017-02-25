package ru.chicker.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.chicker.entities.LoanApplication;

@Repository
public interface LoanApplicationRepository extends CrudRepository<LoanApplication, Long> {
    /**
     * Use this only for testing purpose, because it uses the assumption about the order of ids 
     * records
     * @param personalId the personal id of the client
     * @return the last loan application that was created by this client
     */
    LoanApplication findFirst1ByPersonalIdOrderByIdDesc(String personalId);
}
