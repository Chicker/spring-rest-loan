package ru.chicker.services;

import org.springframework.beans.factory.annotation.Autowired;
import ru.chicker.entities.LimitOfRequests;
import ru.chicker.repositories.BlacklistedPersonRepository;
import ru.chicker.repositories.LimitOfRequestsRepository;

import java.time.LocalDateTime;

public class LoansServiceImpl implements LoansService {
    @Autowired
    private BlacklistedPersonRepository blacklistedPersonRepository;

    @Autowired
    private LimitOfRequestsRepository limitOfRequestsRepository;

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
}
