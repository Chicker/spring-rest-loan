package ru.chicker.services.internal;

import java.util.concurrent.TimeoutException;

public interface InfoByIpProvider {
    String getCountryCode(String ipAddress) throws TimeoutException;
}
