package ru.chicker.services;

import java.util.Optional;

public interface InfoByIpService {
    String getCountryCode(Optional<String> ipAddress);
}
