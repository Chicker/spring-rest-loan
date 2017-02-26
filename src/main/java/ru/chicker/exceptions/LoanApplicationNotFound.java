package ru.chicker.exceptions;

public class LoanApplicationNotFound extends Exception {
    public LoanApplicationNotFound(Long id) {
        super(String.format("Loan application with id = %d is not exist!", id));
    }
}
