package ru.chicker.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.chicker.services.LoansService;
import ru.chicker.services.InfoByIpService;

import static org.mockito.Mockito.mock;

@Configuration
public class TestServiceConfig {
    @Bean
    LoansService getLoansService() {
        return mock(LoansService.class);
    }
    
    @Bean
    InfoByIpService getInfoByIpService() {
        return mock(InfoByIpService.class);
    }
}
