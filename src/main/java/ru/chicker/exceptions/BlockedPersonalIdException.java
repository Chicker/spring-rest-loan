package ru.chicker.exceptions;

public class BlockedPersonalIdException extends Exception {
    public BlockedPersonalIdException(String personalId) {
        super(String.format("The personal id [%s] is in a black list!", personalId));
    }
}
