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
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Created by mirco on 11.07.16.
 */

@Configuration
@EnableJpaRepositories("eu.hbp.mip.repositories")
@EnableTransactionManagement
@EntityScan(basePackages = "eu.hbp.mip.model")
public class PersistenceConfiguration {

    @Value("#{'${spring.featuresDatasource.main-table:features}'}")
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
        //em.setPackagesToScan("eu.hbp.mip.model");
        em.setPersistenceUnitName("portal");
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory().getNativeEntityManagerFactory());
        return txManager;
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
