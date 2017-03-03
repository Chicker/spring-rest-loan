package ru.chicker.services;

import org.springframework.beans.factory.annotation.Autowired;
import ru.chicker.entities.LimitOfRequests;
import ru.chicker.entities.dao.LoanApplicationDao;
import ru.chicker.exceptions.LoanApplicationHasBeenResolvedException;
import ru.chicker.repositories.BlacklistedPersonRepository;
import ru.chicker.repositories.DecisionOnLoanApplicationRepositoryDao;
import ru.chicker.repositories.LimitOfRequestsRepository;
import ru.chicker.repositories.LoanApplicationRepositoryDao;

import java.time.LocalDateTime;
import java.util.List;

public class LoansServiceImpl implements LoansService {
    @Autowired
    private BlacklistedPersonRepository blacklistedPersonRepository;

    @Autowired
    private LimitOfRequestsRepository limitOfRequestsRepository;

    @Autowired
    private DecisionOnLoanApplicationRepositoryDao decisionsRepo;

    @Autowired
    private LoanApplicationRepositoryDao loansRepo;

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
    public void resolveLoanApplication(Long loanApplicationId, boolean approve)
    throws LoanApplicationHasBeenResolvedException {
        if (null != decisionsRepo.findByLoanApplication(loanApplicationId)) {
            throw new LoanApplicationHasBeenResolvedException(loanApplicationId);
        }

        decisionsRepo.addDecision(loanApplicationId, LocalDateTime.now(), approve);
    }

    @Override
    public List<LoanApplicationDao> getLoansByApproved(boolean approved) {
        return loansRepo.findLoansByApproved(approved);
    }

    @Override
    public List<LoanApplicationDao> getLoansByClient(String personalId, boolean approved) {
        return loansRepo.findLoansByClientAndByApproved(personalId, approved);
    }

    @Override
    public LoanApplicationDao findById(Long loanApplicationId) {
        
        return loansRepo.findById(loanApplicationId);
    }

    @Override
    public void addLoanApplication(LoanApplicationDao loanApplication) {
        loansRepo.addLoanApplication(loanApplication);
    }
}
