package ru.chicker.services;

import javaslang.control.Try;
import ru.chicker.services.internal.InfoByIpFreeGeoIpProvider;
import ru.chicker.services.internal.InfoByIpIpApiProvider;
import ru.chicker.services.internal.InfoByIpProvider;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class InfoByIpServiceImpl implements InfoByIpService {

    private final InfoByIpFreeGeoIpProvider freeGeoIpProvider;
    private final InfoByIpIpApiProvider infoByIpIpApiProvider;

    public InfoByIpServiceImpl(InfoByIpFreeGeoIpProvider freeGeoIpProvider,
                               InfoByIpIpApiProvider infoByIpIpApiProvider) {
        this.freeGeoIpProvider = freeGeoIpProvider;
        this.infoByIpIpApiProvider = infoByIpIpApiProvider;
    }

    @Override
    public String getCountryCode(Optional<String> ipAddress) {
        String fallbackCountryCode = "lv";

        if (!ipAddress.isPresent()) return fallbackCountryCode;

        List<InfoByIpProvider> ipServiceList = Arrays.asList(freeGeoIpProvider, infoByIpIpApiProvider);

        for (InfoByIpProvider infoByIpProvider : ipServiceList) {
            Try<String> result = infoByIpProvider.getCountryCode(ipAddress.get());
            if (result.isSuccess()) return result.get();
            // if the service has returned error then we will try another service
            // TODO Log about no accessibility of the service
        }

        // return fallback country code if the all services are not accessible
        return fallbackCountryCode;
    }
}
