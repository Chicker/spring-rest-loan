package ru.chicker.configs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "ru.chicker")
@Import(PersistenceConfig.class)
public class AppConfig {
}
