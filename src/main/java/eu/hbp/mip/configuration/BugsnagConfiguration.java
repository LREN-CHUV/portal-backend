package eu.hbp.mip.configuration;

import ch.chuv.lren.mip.portal.Reporting;
import com.bugsnag.Bugsnag;
import com.bugsnag.BugsnagSpringConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(BugsnagSpringConfiguration.class)
public class BugsnagConfiguration {
    static {
        new Reporting().init();
    }

    @Bean
    public Bugsnag bugsnag() {
        return new Bugsnag(System.getenv("BUGSNAG_KEY"));
    }
}
