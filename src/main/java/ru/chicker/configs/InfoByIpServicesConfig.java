package ru.chicker.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.chicker.services.internal.InfoByIpFreeGeoIpProvider;
import ru.chicker.services.internal.InfoByIpIpApiProvider;

@Configuration
public class InfoByIpServicesConfig {
    @Bean
    InfoByIpFreeGeoIpProvider getInfoByIpFreeGeoIpProvider() {
        return new InfoByIpFreeGeoIpProvider();
    }
    
    @Bean
    InfoByIpIpApiProvider getInfoByIpIpApiProvider() {
        return new InfoByIpIpApiProvider();
    }
}
