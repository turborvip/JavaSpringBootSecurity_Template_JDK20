package com.turborvip.security.application.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@PropertySource({"classpath:application.properties"})
@ComponentScan({"com.turborvip.security"})
@EnableJpaRepositories(basePackages = "com.turborvip.security.application.repositories")
@Slf4j
public class Persistence {

    private static final HikariConfig hikariConfig = new HikariConfig();
    private static HikariDataSource hikariDataSource;
    @Autowired
    private Environment env;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        // provider entity package name
        entityManagerFactoryBean.setPackagesToScan("com.turborvip.security.domain.entity");

        final HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactoryBean.setJpaProperties(additionalProperties());

        return entityManagerFactoryBean;
    }

    final Properties additionalProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        hibernateProperties.setProperty("hibernate.hbm2dll.create_namespaces", env.getProperty("hibernate.hbm2dll.create_namespaces"));
        hibernateProperties.setProperty("hibernate.show_sql", env.getProperty("hibernate.showSQL"));
        hibernateProperties.setProperty("hibernate.format_sql", env.getProperty("hibernate.formatSQL"));
//        hibernateProperties.setProperty("hibernate.cache.use_second_level_cache", env.getProperty("hibernate.cache.use_second_level_cache"));
//        hibernateProperties.setProperty("hibernate.cache.use_query_cache", env.getProperty("hibernate.cache.use_query_cache"));
        // hibernateProperties.setProperty("hibernate.globally_quoted_identifiers", "true");
        return hibernateProperties;
    }

    @Bean
    public DataSource dataSource() {
        hikariConfig.setDriverClassName(env.getProperty("jdbc.driverClassName"));
        hikariConfig.setJdbcUrl(env.getProperty("jdbc.url"));
        hikariConfig.setUsername(env.getProperty("jdbc.user"));
        hikariConfig.setPassword(env.getProperty("jdbc.pass"));
        hikariConfig.setPoolName(env.getProperty("hikari.poolName"));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(Objects.requireNonNull(env.getProperty("hikari.maxPoolSize"))));
        // time which is calculator from not activity, after that time connection was move to pool!
        hikariConfig.setIdleTimeout(Long.parseLong(Objects.requireNonNull(env.getProperty("hikari.timeout"))));
        hikariConfig.setAutoCommit(Boolean.parseBoolean(env.getProperty("hikari.autoCommit")));
        hikariConfig.setMaxLifetime(Long.parseLong(Objects.requireNonNull(env.getProperty("hikari.maxLifeTime"))));
        hikariConfig.setConnectionTimeout(Long.parseLong(Objects.requireNonNull(env.getProperty("hikari.connectionTimeout"))));
        hikariDataSource = new HikariDataSource(hikariConfig);
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public PlatformTransactionManager transactionManager(final EntityManagerFactory emf) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public void getConnection() throws SQLException {
        log.info(String.valueOf(hikariDataSource.getConnection()));
        log.info("Memory : {}", Runtime.getRuntime().totalMemory());
        log.info("Processor : {}", Runtime.getRuntime().availableProcessors());
    }

}