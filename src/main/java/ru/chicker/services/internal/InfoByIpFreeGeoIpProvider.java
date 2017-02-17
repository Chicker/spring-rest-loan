package ru.chicker.services.internal;

import ru.chicker.services.InfoByIpService;

import java.util.concurrent.TimeoutException;

public class InfoByIpFreeGeoIpProvider implements InfoByIpProvider {
    @Override
    public String getCountryCode(String ipAddress) throws TimeoutException {
        // stub
        throw new TimeoutException("The service freegeoip.net does not accessible within timeout.");
    }
}
