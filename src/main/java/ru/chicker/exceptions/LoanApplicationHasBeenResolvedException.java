package ru.chicker.exceptions;

public class LoanApplicationHasBeenResolvedException extends Exception {
    public LoanApplicationHasBeenResolvedException(Long loanAppId) {
        super(String.format("Loan application with id equals %d has been resolved!", loanAppId));
    }
}
