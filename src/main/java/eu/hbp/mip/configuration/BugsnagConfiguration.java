package eu.hbp.mip.configuration;

import com.bugsnag.Bugsnag;
import com.bugsnag.BugsnagSpringConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(BugsnagSpringConfiguration.class)
public class BugsnagConfiguration {
    @Bean
    public Bugsnag bugsnag() {
        return new Bugsnag("dff301aa15eb795a6d8b22b600586f77");
    }
}