package ru.chicker.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.chicker.services.ILoansService;
import ru.chicker.services.LoansService;

@Configuration
@ComponentScan("ru.chicker.services")
public class ServiceConfig {
    @Bean
    ILoansService getLoansService() {
        return new LoansService();
    }
}
