package com.paydaytrade.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.paydaytrade.data.repository",
        entityManagerFactoryRef = "paydaytrade-em",
        transactionManagerRef = "paydaytrade-tm"
)
public class DatabaseConfiguration {

    @Value("${payday-trade.datasource.username}")
    private String datasourceUser;
    @Value("${payday-trade.datasource.password}")
    private String datasourcePassword;
    @Value("${payday-trade.datasource.url}")
    private String datasourceHost;
    @Value("${payday-trade.datasource.driver}")
    private String datasourceDriver;
    @Value("${payday-trade.hibernate.database-platform}")
    private String hibernateDatabasePlatform;
    @Value("${payday-trade.hibernate.show-sql}")
    private boolean hibernateShowSql;
    @Value("${payday-trade.hibernate.format-sql}")
    private boolean hibernateFormatSql;
    @Value("${payday-trade.hibernate.ddl-auto}")
    private String hibernateDdlAuto;

    @Bean
    public DataSource dataSourceSphere() {
        HikariConfig config = new HikariConfig();
        config.setUsername(datasourceUser);
        config.setPassword(datasourcePassword);
        config.setJdbcUrl(datasourceHost);
        config.setDriverClassName(datasourceDriver);
        return new HikariDataSource(config);
    }

    @Bean(name = "paydaytrade-em")
    public LocalContainerEntityManagerFactoryBean entityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSourceSphere());
        em.setPackagesToScan("com.paydaytrade.data.entity");
        em.setJpaDialect(new HibernateJpaDialect());
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", hibernateDdlAuto);
        properties.put("hibernate.dialect", hibernateDatabasePlatform);
        properties.put("hibernate.show_sql", hibernateShowSql);
        properties.put("hibernate.format_sql", hibernateFormatSql);
        em.setJpaPropertyMap(properties);
        return em;
    }

    @Bean(name = "paydaytrade-tm")
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManager().getObject());
        return transactionManager;
    }

}