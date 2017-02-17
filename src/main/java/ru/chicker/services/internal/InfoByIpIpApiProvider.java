package ru.chicker.services.internal;

import ru.chicker.services.InfoByIpService;

import java.util.concurrent.TimeoutException;

public class InfoByIpIpApiProvider implements InfoByIpProvider {
    @Override
    public String getCountryCode(String ipAddress) throws TimeoutException {
        // stub
        return "ru";
    }
}
