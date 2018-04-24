package eu.hbp.mip.configuration;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.ExtendedActorSystem;
import akka.cluster.Cluster;
import akka.cluster.pubsub.DistributedPubSub;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ApplicationContext applicationContext;

    @Value("#{'${akka.woken.host:woken}'}")
    private String wokenHost;

    @Value("#{'${akka.woken.port:8088}'}")
    private String wokenPort;

    @Value("#{'${akka.woken.path:/user/entrypoint}'}")
    private String wokenPath;

    @Bean
    public ExtendedActorSystem actorSystem() {
        Config config = ConfigFactory.load("application.conf");
        ExtendedActorSystem system = (ExtendedActorSystem) ActorSystem.create("woken", config);
        SPRING_EXTENSION_PROVIDER.get(system).initialize(applicationContext);
        return system;
    }

    @Bean
    public Cluster cluster() {
        return new Cluster(actorSystem());
    }

    @Bean
    public String wokenReceptionistPath() {
        return "akka.tcp://woken@" + wokenHost + ":" + wokenPort + "/system/receptionist";
    }

    @Bean
    public ActorRef wokenMediator() {
        LOGGER.info("Start Woken client " + wokenReceptionistPath());
        return DistributedPubSub.get(actorSystem()).mediator();
    }

}
