package ru.chicker.services.internal;

import javaslang.control.Try;

public interface InfoByIpProvider {
    Try<String> getCountryCode(String ipAddress);
}
