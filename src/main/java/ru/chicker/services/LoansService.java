package ru.chicker.services;

import ru.chicker.entities.dao.LimitOfRequestsDao;
import ru.chicker.entities.dao.LoanApplicationDao;
import ru.chicker.exceptions.LoanApplicationHasBeenResolvedException;

import java.time.LocalDateTime;
import java.util.List;

public interface LoansService {
    Boolean personalIdIsInBlackList(String personalId);

    boolean checkLimitAndIncrement(String countryCode);

    LimitOfRequestsDao getActualLimitOfRequests(String countryCode);

    LimitOfRequestsDao getLimitOfRequestsOnDate(String countryCode, LocalDateTime date);

    /**
     * Accept or decline loan application.
     * Note. For given loan application should not be exist a record in the decisions table! 
     * @param loanApplicationId 
     * @param approve if it false then the loan application be declined
     */
    void resolveLoanApplication(Long loanApplicationId, boolean approve) throws
                                                                                  LoanApplicationHasBeenResolvedException;

    List<LoanApplicationDao> getLoansByApproved(boolean approved);

    List<LoanApplicationDao> getLoansByClient(String personalId, boolean approved);

    LoanApplicationDao findById(Long loanApplicationId);

    void addLoanApplication(LoanApplicationDao loanApplication);
}
