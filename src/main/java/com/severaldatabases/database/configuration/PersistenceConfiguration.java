package com.severaldatabases.database.configuration;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@PropertySource({"classpath:application.properties"})
@EnableJpaRepositories(
        basePackages = "com.severaldatabases.repository",
        entityManagerFactoryRef = "entityManager",
        transactionManagerRef = "transactionManager")
public class PersistenceConfiguration {

    /* === MAIN === */
    @Bean(name = {"transactionManager", "mainTransactionManager"})
    @Profile("main")
    public PlatformTransactionManager mainTransactionManager(
            @Qualifier("mainEntityManager") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return transactionManager(entityManagerFactory);
    }

    @Bean(name = {"entityManager", "mainEntityManager"})
    @Profile("main")
    public LocalContainerEntityManagerFactoryBean mainEntityManager(
            @Qualifier("mainDataSource") DataSource datasource) {
        LocalContainerEntityManagerFactoryBean emfb = entityManagerFactoryBean(datasource);
        emfb.setJpaPropertyMap(properties("spring.main-datasource.hibernate.dialect"));
        return emfb;
    }

    @Bean(name = {"mainDataSource"})
    @Profile("main")
    @ConfigurationProperties(prefix = "spring.main-datasource")
    public DataSource mainDataSource() {
        return DataSourceBuilder.create().build();
    }

    /* === TEST === */
    @Bean(name = "testTransactionManager")
    @Profile("test")
    public PlatformTransactionManager testTransactionManager(
            @Qualifier("testEntityManager") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return transactionManager(entityManagerFactory);
    }

    @Bean(name = "testEntityManager")
    @Profile("test")
    public LocalContainerEntityManagerFactoryBean testEntityManager(
            @Qualifier("testDataSource") DataSource datasource) {
        LocalContainerEntityManagerFactoryBean emfb = entityManagerFactoryBean(datasource);
        emfb.setJpaPropertyMap(properties("spring.test-datasource.hibernate.dialect"));
        return emfb;
    }

    @Bean(name = "testDataSource")
    @Profile("test")
    @ConfigurationProperties(prefix = "spring.test-datasource")
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }

    /* === OTHER === */
    @Bean(name = {"transactionManager", "otherTransactionManager"})
    @Profile("other")
    public PlatformTransactionManager otherTransactionManager(
            @Qualifier("otherEntityManager") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return transactionManager(entityManagerFactory);
    }

    @Bean(name = {"entityManager", "otherEntityManager"})
    @Profile("other")
    public LocalContainerEntityManagerFactoryBean otherEntityManager(
            @Qualifier("otherDataSource") DataSource datasource) {
        LocalContainerEntityManagerFactoryBean emfb = entityManagerFactoryBean(datasource);
        emfb.setJpaPropertyMap(properties("spring.other-datasource.hibernate.dialect"));
        return emfb;
    }

    @Bean(name = "otherDataSource")
    @Profile("other")
    @ConfigurationProperties(prefix = "spring.other-datasource")
    public DataSource otherDataSource() {
        return DataSourceBuilder.create().build();
    }

    /* === ----- === */
    private LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(final DataSource datasource) {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(datasource);
        em.setPackagesToScan("com.severaldatabases.model");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return em;
    }

    private PlatformTransactionManager transactionManager(final LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return transactionManager;
    }

    private HashMap<String, String> properties(final String propertyname4Dialect) {
        final HashMap<String, String> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty(propertyname4Dialect));
        return properties;
    }

    @Autowired
    private Environment env;
}
