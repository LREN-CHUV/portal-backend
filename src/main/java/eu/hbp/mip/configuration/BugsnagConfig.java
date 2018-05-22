package eu.hbp.mip.configuration;

import com.bugsnag.Bugsnag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BugsnagConfig {
    @Bean
    public Bugsnag bugsnag() {
        return new Bugsnag("dff301aa15eb795a6d8b22b600586f77");
    }
}