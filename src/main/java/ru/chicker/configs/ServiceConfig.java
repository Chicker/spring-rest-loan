package ru.chicker.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.chicker.services.LoansService;
import ru.chicker.services.InfoByIpService;
import ru.chicker.services.InfoByIpServiceImpl;
import ru.chicker.services.LoansServiceImpl;
import ru.chicker.services.internal.InfoByIpFreeGeoIpProvider;
import ru.chicker.services.internal.InfoByIpIpApiProvider;

@Configuration
public class ServiceConfig {
    @Bean
    LoansService getLoansService() {
        return new LoansServiceImpl();
    }

    @Bean
    InfoByIpService getInfoByIpService(InfoByIpFreeGeoIpProvider freeGeoIpProvider,
                                       InfoByIpIpApiProvider infoByIpIpApiProvider) {
        return new InfoByIpServiceImpl(freeGeoIpProvider, infoByIpIpApiProvider);
    }
}
