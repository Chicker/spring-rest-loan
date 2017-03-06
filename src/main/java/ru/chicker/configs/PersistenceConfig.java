package ru.chicker.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.chicker.repositories.BaseRepositoryImpl;


import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(value = "ru.chicker.repositories",
    repositoryBaseClass = BaseRepositoryImpl.class)
@PropertySource("classpath:application.properties")
public class PersistenceConfig {
    
    @Autowired
    Environment env;
    
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory
        (DataSource ds){
        LocalContainerEntityManagerFactoryBean emf =
            new LocalContainerEntityManagerFactoryBean();
        
        emf.setDataSource(ds);
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        
        emf.setPackagesToScan("ru.chicker.entities");

        Properties jpaProperties = new Properties();

        jpaProperties.put("hibernate.hbm2ddl.auto",
            env.getRequiredProperty("hibernate.hbm2ddl.auto"));
        jpaProperties.put("hibernate.dialect",
            env.getRequiredProperty("hibernate.dialect"));
        jpaProperties.put("hibernate.show_sql",
            env.getRequiredProperty("hibernate.show_sql"));
        jpaProperties.put("hibernate.format_sql",
            env.getRequiredProperty("hibernate.format_sql"));
        
        emf.setJpaProperties(jpaProperties);
        emf.afterPropertiesSet();
        
        return emf;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }
}
