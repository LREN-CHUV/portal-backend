/**
 * Created by mirco on 04.12.15.
 */

package eu.hbp.mip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MIPApplication {

    public static void main(String[] args) {
        SpringApplication.run(MIPApplication.class, args);
    }

}