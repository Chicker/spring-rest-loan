package ru.chicker.exceptions;

public class LimitOfRequestsExceededException extends Exception {
    public LimitOfRequestsExceededException(String countryCode) {
        super(String.format("The limit of requests for the loan application for %s country is " +
            "exceeded!", countryCode));
    }
}
