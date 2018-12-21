/*
 * Created by mirco on 04.12.15.
 */

package eu.hbp.mip;

import eu.hbp.mip.configuration.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@EnableAutoConfiguration(exclude = { ValidationAutoConfiguration.class })
@Import({ AkkaConfiguration.class, BugsnagConfiguration.class,
        CacheConfiguration.class, PersistenceConfiguration.class,
        SecurityConfiguration.class, WebConfiguration.class})
public class MIPApplication {

    public static void main(String[] args) {
        SpringApplication.run(MIPApplication.class, args);
    }

}
