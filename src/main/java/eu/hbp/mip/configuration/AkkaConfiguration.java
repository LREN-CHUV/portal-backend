package eu.hbp.mip.configuration;

import akka.actor.ActorSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static eu.hbp.mip.akka.SpringExtension.SpringExtProvider;

/**
 * Created by mirco on 24.10.16.
 */

@Configuration
class AkkaConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public ActorSystem actorSystem() {
        ActorSystem system = ActorSystem.create("AkkaActorSystem");
        SpringExtProvider.get(system).initialize(applicationContext);
        return system;
    }

}