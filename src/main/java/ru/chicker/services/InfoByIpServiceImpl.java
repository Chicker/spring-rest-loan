package ru.chicker.services;

import ru.chicker.services.internal.InfoByIpFreeGeoIpProvider;
import ru.chicker.services.internal.InfoByIpIpApiProvider;
import ru.chicker.services.internal.InfoByIpProvider;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

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

        for (InfoByIpProvider infoByIpProvider: ipServiceList) {
            try {
                return infoByIpProvider.getCountryCode(ipAddress.get());
            } catch (TimeoutException ex) {
                // Log about no accessibility of the service and will try other service
                System.out.println(ex);
            }
        }

        // return fallback country code if the all services are not accessible
        return fallbackCountryCode;
    }
}
