package ru.chicker.services;

public class LoansService implements ILoansService {
    @Override
    public Boolean personalIdIsInBlackList(String personalId) {
        if (personalId.equals("blacklist")) {
            return true;
        } else {
            return false;
        }
    }
}
