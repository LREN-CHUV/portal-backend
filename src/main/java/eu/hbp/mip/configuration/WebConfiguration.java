package eu.hbp.mip.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by mirco on 11.07.16.
 */

@Configuration
@EnableSwagger2
@EnableSpringDataWebSupport
@DependsOn("wokenCluster")
public class WebConfiguration {

    @Bean
    public UiConfiguration uiConfig() {
        return UiConfiguration.DEFAULT;
    }

    @Bean
    public Docket documentation() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("eu.hbp.mip.controllers"))
                .build()
                .pathMapping("/")
                .apiInfo(metadata());
    }

    private ApiInfo metadata() {
        return new ApiInfoBuilder()
                .title("Medical Informatics Platform API")
                .description("Serve the MIP Frontend")
                .version("1.0")
                .contact(new Contact("Mirco Nasuti", "https://www.unil.ch/lren/en/home.html", "mirco.nasuti@chuv.ch"))
                .build();
    }
}
