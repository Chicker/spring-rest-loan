package ru.chicker.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.chicker.domain.entities.LoanApplication;

import java.util.List;

@Repository
public interface LoanApplicationRepository extends BaseRepository<LoanApplication, Long> {
    /**
     * Use this only for testing purpose, because it uses the assumption about the order of ids
     * records
     *
     * @param personalId the personal id of the client
     * @return the last loan application that was created by this client
     */
    LoanApplication findFirst1ByPersonalIdOrderByIdDesc(String personalId);

//    List<LoanApplication> findByPersonalId(String personalId);

    @Query("select loan from LoanApplication\n" +
        "loan left join DecisionOnLoanApplication d on loan.id = d.loanApplication\n" +
        "where loan.personalId = :personalId")
    List<LoanApplication> findByPersonalId(@Param("personalId") String personalId);
}
