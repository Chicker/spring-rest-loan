package ru.chicker.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.chicker.services.ILoansService;

import static org.mockito.Mockito.mock;

@Configuration
public class TestServiceConfig {
    @Bean
    ILoansService getLoansService() {
        return mock(ILoansService.class);
    }
}
