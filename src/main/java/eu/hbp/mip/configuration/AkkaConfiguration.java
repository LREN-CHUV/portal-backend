package eu.hbp.mip.configuration;

import akka.actor.ActorSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static eu.hbp.mip.akka.SpringExtension.SPRING_EXTENSION_PROVIDER;

/**
 * Created by mirco on 24.10.16.
 */

@Configuration
@ComponentScan
class AkkaConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Value("#{'${akka.woken.host:woken}'}")
    private String wokenHost;

    @Value("#{'${akka.woken.port:8088}'}")
    private String wokenPort;

    @Value("#{'${akka.woken.path:/user/entrypoint}'}")
    private String wokenPath;

    @Bean
    public ActorSystem actorSystem() {
        ActorSystem system = ActorSystem.create("AkkaActorSystem");
        SPRING_EXTENSION_PROVIDER.get(system).initialize(applicationContext);
        return system;
    }

    @Bean String wokenRefPath() {
        return "akka.tcp://woken@"+wokenHost+":"+wokenPort+wokenPath;
    }

}