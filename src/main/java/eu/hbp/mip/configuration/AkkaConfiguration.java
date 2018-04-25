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
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;
import java.util.List;

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

    private final Config config = ConfigFactory.load("application.conf");

    @Bean
    public ExtendedActorSystem actorSystem() {
        LOGGER.info("Create actor system at " + wokenClusterHost() + ":" + wokenClusterPort());
        ExtendedActorSystem system = (ExtendedActorSystem) ActorSystem.create("woken", config);
        SPRING_EXTENSION_PROVIDER.get(system).initialize(applicationContext);
        return system;
    }

    @Bean
    @Lazy
    public Cluster wokenCluster() {
        return Cluster.get(actorSystem());
    }

    @Bean
    public String wokenClusterHost() {
        return config.getString("clustering.ip");
    }

    @Bean
    public Integer wokenClusterPort() {
        return config.getInt("clustering.port");
    }

    @Bean
    public List<String> wokenPath() {
        return config.getStringList("akka.cluster.seed-nodes");
    }

    @Bean
    @Lazy
    public ActorRef wokenMediator() {
        LOGGER.info("Connect to Woken cluster nodes at " + String.join(",", wokenPath()));
        return DistributedPubSub.get(actorSystem()).mediator();
    }

}
