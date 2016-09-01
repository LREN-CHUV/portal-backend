package eu.hbp.mip.configuration;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

/**
 * Created by mirco on 11.07.16.
 */

@Configuration
@EnableJpaRepositories(value = "eu.hbp.mip.repositories")
@EntityScan(basePackages = "eu.hbp.mip.model")
public class PersistenceConfiguration {

    @Bean(name = "datasource")
    @Primary
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "metaDatasource")
    @ConfigurationProperties(prefix="spring.variablesDatasource")
    public DataSource metaDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Autowired
    @Qualifier("metaJDBC")
    public JdbcTemplate metaJDBC() {
        return new JdbcTemplate(metaDataSource());
    }

    @Bean(name = "metaEntityManagerFactory")
    @DependsOn("flyway")
    public LocalContainerEntityManagerFactoryBean metaEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        return em;
    }

    @Bean(name = "metaMigrations", initMethod = "migrate")
    public Flyway metaMigrations() {
        Flyway flyway = new Flyway();
        flyway.setBaselineOnMigrate(true);
        flyway.setDataSource(metaDataSource());
        return flyway;
    }

}
