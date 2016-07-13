package org.hbp.mip.configuration;

import org.hbp.mip.utils.CSVUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by mirco on 11.07.16.
 */

@Configuration
@EnableJpaRepositories(value = "org.hbp.mip.repositories")
@EntityScan(basePackages = "org.hbp.mip.model")
public class PersistenceConfiguration {

    @Value("#{'${spring.datasource.username:postgres}'}")
    String dbUser;

    @Value("#{'${spring.datasource.password:pass}'}")
    String dbPass;

    @Value("#{'${spring.datasource.url:jdbc:postgresql://db:5432/postgres}'}")
    String dbUrl;

    @Value("#{'${spring.datasource.driver-class-name:org.postgresql.Driver}'}")
    String dbDriver;

    @Bean
    public CSVUtil csvUtil() {
        return new CSVUtil();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());
        return em;
    }

    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.show_sql", "true");
        return properties;
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .username(dbUser)
                .password(dbPass)
                .url(dbUrl)
                .driverClassName(dbDriver)
                .build();
    }
}
