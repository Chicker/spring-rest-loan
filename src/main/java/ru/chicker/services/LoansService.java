package ru.chicker.services;

import org.springframework.beans.factory.annotation.Autowired;
import ru.chicker.repositories.BlacklistedPersonRepository;

public class LoansService implements ILoansService {
    @Autowired
    BlacklistedPersonRepository blacklistedPersonRepository;

    @Override
    public Boolean personalIdIsInBlackList(String personalId) {
        return null != blacklistedPersonRepository.findOne(personalId);
    }
}
