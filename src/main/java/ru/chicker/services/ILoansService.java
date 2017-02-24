package ru.chicker.services;

import ru.chicker.entities.LimitOfRequests;

import java.time.LocalDateTime;

public interface ILoansService {
    Boolean personalIdIsInBlackList(String personalId);

    boolean checkLimitAndIncrement(String countryCode);

    LimitOfRequests getActualLimitOfRequests(String countryCode);

    LimitOfRequests getLimitOfRequestsOnDate(String countryCode, LocalDateTime date);
}
