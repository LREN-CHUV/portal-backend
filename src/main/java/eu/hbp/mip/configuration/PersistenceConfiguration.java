package eu.hbp.mip.configuration;

import eu.hbp.mip.utils.DataUtil;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.*;
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

    @Value("#{'${spring.featuresDatasource.main-table:adni_merge}'}")
    private String featuresMainTable;

    @Primary
    @Bean(name = "portalDatasource")
    @ConfigurationProperties(prefix="spring.portalDatasource")
    public DataSource portalDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "metaDatasource")
    @ConfigurationProperties(prefix="spring.metaDatasource")
    public DataSource metaDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "featuresDatasource")
    @ConfigurationProperties(prefix="spring.featuresDatasource")
    public DataSource featuresDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Autowired
    @Qualifier("metaJdbcTemplate")
    public JdbcTemplate metaJdbcTemplate() {
        return new JdbcTemplate(metaDataSource());
    }

    @Bean
    @Autowired
    @Qualifier("featuresJdbcTemplate")
    public JdbcTemplate featuresJdbcTemplate() {
        return new JdbcTemplate(featuresDataSource());
    }

    @Bean(name = "entityManagerFactory")
    @DependsOn("flyway")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(portalDataSource());
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        return em;
    }

    @Bean(name = "flyway", initMethod = "migrate")
    public Flyway migrations() {
        Flyway flyway = new Flyway();
        flyway.setBaselineOnMigrate(true);
        flyway.setDataSource(portalDataSource());
        return flyway;
    }

    @Bean(name = "dataUtil")
    @Scope("singleton")
    public DataUtil dataUtil() {
        return new DataUtil(featuresJdbcTemplate(), featuresMainTable);
    }

}
