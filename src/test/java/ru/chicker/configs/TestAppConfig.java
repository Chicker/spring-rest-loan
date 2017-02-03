package ru.chicker.configs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("ru.chicker")
@Import({PersistenceConfig.class})
public class TestAppConfig {
}
