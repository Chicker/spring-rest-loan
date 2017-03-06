package ru.chicker.services;

import ru.chicker.entities.LimitOfRequests;
import ru.chicker.entities.LoanApplication;
import ru.chicker.exceptions.LoanApplicationHasBeenResolvedException;

import java.time.LocalDateTime;
import java.util.List;

public interface LoansService {
    Boolean personalIdIsInBlackList(String personalId);

    boolean checkLimitAndIncrement(String countryCode);

    LimitOfRequests getActualLimitOfRequests(String countryCode);

    LimitOfRequests getLimitOfRequestsOnDate(String countryCode, LocalDateTime date);

    /**
     * Accept or decline loan application.
     * Note. For given loan application should not be exist a record in the decisions table! 
     * @param loanApplication 
     * @param approve if it false then the loan application be declined
     */
    void resolveLoanApplication(LoanApplication loanApplication, boolean approve) throws
                                                                                  LoanApplicationHasBeenResolvedException;

    List<LoanApplication> getLoansByApproved(boolean approved);

    List<LoanApplication> getLoansByClient(String personalId, boolean approved);
    void deleteLoanApplication(LoanApplication loanApplication);
}
