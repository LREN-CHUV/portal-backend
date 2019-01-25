/*
 * Created by mirco on 04.12.15.
 */

package eu.hbp.mip;

import ch.chuv.lren.mip.portal.Reporting;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MIPApplication {

    public static void main(String[] args) {
        new Reporting().init();
        SpringApplication.run(MIPApplication.class, args);
    }

}
