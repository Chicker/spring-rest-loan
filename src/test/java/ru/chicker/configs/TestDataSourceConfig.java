package ru.chicker.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

@Configuration
public class TestDataSourceConfig {
    @Bean(destroyMethod = "shutdown")
    public DataSource testDataSource() {
        EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
            .generateUniqueName(true)
            .setType(H2)
            .setScriptEncoding("UTF-8")
            .ignoreFailedDrops(true)
            .addScript("schema.sql")
            .addScripts("persons_data.sql",
                "blacklist_personal_ids.sql",
                "initial_data_for_tests.sql")
            .build();
        return db;
    }
}
