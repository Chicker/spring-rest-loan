package ru.chicker.services;

import org.springframework.beans.factory.annotation.Autowired;
import ru.chicker.entities.DecisionOnLoanApplication;
import ru.chicker.entities.LimitOfRequests;
import ru.chicker.entities.LoanApplication;
import ru.chicker.exceptions.LoanApplicationHasBeenResolvedException;
import ru.chicker.repositories.BlacklistedPersonRepository;
import ru.chicker.repositories.DecisionOnLoanApplicationRepository;
import ru.chicker.repositories.LimitOfRequestsRepository;

import java.time.LocalDateTime;

public class LoansServiceImpl implements LoansService {
    @Autowired
    private BlacklistedPersonRepository blacklistedPersonRepository;

    @Autowired
    private LimitOfRequestsRepository limitOfRequestsRepository;

    @Autowired
    private DecisionOnLoanApplicationRepository decisionsRepo;

    @Override
    public Boolean personalIdIsInBlackList(String personalId) {
        return null != blacklistedPersonRepository.findOne(personalId);
    }

    /**
     * Checks the limit of the requests for loan application for given code of country.
     * Every call of this method will increment count of requests
     *
     * @param countryCode
     * @return true if the limit of the requests for given code of country is exceeded,
     * otherwise returns false.
     */
    @Override
    public boolean checkLimitAndIncrement(String countryCode) {
        LocalDateTime nowDateTime = LocalDateTime.now();

        LimitOfRequests limitOfRequests = getActualLimitOfRequests(countryCode);

        if (null == limitOfRequests) {
            // if the actual limit for specified code of country is not exist
            return false;
        }

        long newRequestsCount = limitOfRequests.getRequested() + 1; // include current request
        long requestsLimit = limitOfRequests.getRequestsLimit();

        // increment the count of requests for loan application and save it to DB 
        limitOfRequests.setRequested(newRequestsCount);
        limitOfRequestsRepository.save(limitOfRequests);

        return newRequestsCount > requestsLimit;
    }

    public LimitOfRequests getActualLimitOfRequests(String countryCode) {
        LocalDateTime dateTimeNow = LocalDateTime.now();
        return limitOfRequestsRepository
            .findByCountryCodeAndStartDateIsLessThanEqualAndEndDateIsGreaterThanEqual(
                countryCode, dateTimeNow, dateTimeNow);
    }

    public LimitOfRequests getLimitOfRequestsOnDate(String countryCode, LocalDateTime date) {
        return limitOfRequestsRepository
            .findByCountryCodeAndStartDateIsLessThanEqualAndEndDateIsGreaterThanEqual(
                countryCode, date, date);
    }

    @Override
    public void resolveLoanApplication(LoanApplication loanApplication, boolean approve)
    throws LoanApplicationHasBeenResolvedException {
        if (null != decisionsRepo.findByLoanApplication(loanApplication)) {
            throw new LoanApplicationHasBeenResolvedException(loanApplication.getId());
        }

        int approveInt = approve ? 1 : 0;

        DecisionOnLoanApplication decision = new DecisionOnLoanApplication(LocalDateTime.now(),
            approveInt, loanApplication);

        decisionsRepo.save(decision);
    }
}
