package eu.hbp.mip.configuration;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.ExtendedActorSystem;
import akka.cluster.Cluster;
import akka.cluster.Member;
import akka.cluster.pubsub.DistributedPubSub;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

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

    private final Config config;

    {
        Config remotingConfig = ConfigFactory.parseResourcesAnySyntax("akka-remoting.conf").resolve();
        String remotingImpl = remotingConfig.getString("remoting.implementation");
        config = ConfigFactory
                .parseString("akka {\n" +
                        "  actor.provider = cluster\n" +
                        "  extensions += \"akka.cluster.pubsub.DistributedPubSub\"\n" +
                        "}")
                .withFallback(ConfigFactory.parseResourcesAnySyntax("akka.conf"))
                .withFallback(ConfigFactory.parseResourcesAnySyntax("akka-" + remotingImpl + "-remoting.conf"))
                .withFallback(ConfigFactory.parseResourcesAnySyntax("kamon.conf"))
                .withFallback(ConfigFactory.load())
                .resolve();
    }

    @Bean
    public ExtendedActorSystem actorSystem() {
        LOGGER.info("Step 1/3: Starting actor system...");
        LOGGER.info("Create actor system at " + wokenClusterHost() + ":" + wokenClusterPort());
        ExtendedActorSystem system = (ExtendedActorSystem) ActorSystem.create(wokenClusterName(), config);
        SPRING_EXTENSION_PROVIDER.get(system).initialize(applicationContext);
        return system;
    }

    @Bean
    public Cluster wokenCluster() {
        Cluster cluster = Cluster.get(actorSystem());
        LOGGER.info("Connect to Woken cluster nodes at " + String.join(",", wokenPath()));
        Semaphore semaphore = new Semaphore(1);
        cluster.registerOnMemberUp( () -> {
                    LOGGER.info("Step 2/3: Cluster up, registering the actors...");

                    // Do not call wokenMediator() here to avoid recursive loops
                    ActorRef mediator = DistributedPubSub.get(actorSystem()).mediator();

                    LOGGER.info("Woken Mediator available at " + mediator.path().toStringWithoutAddress());

                    semaphore.release();
                });

        for (Member member: cluster.state().getMembers()) {
            LOGGER.info("Member " + StringUtils.join(member.getRoles(), ",") + " at " + member.address().toString() + " is in the cluster");
        }

        if (cluster.state().members().size() < 2) {
            LOGGER.info("Waiting for Woken cluster connection...");
            try {
                semaphore.tryAcquire(5, TimeUnit.MINUTES);
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                LOGGER.warn("Cannot wait for Akka cluster start", e);
            }
        }

        final Runnable leaveCluster = () -> cluster.leave(cluster.selfAddress());
        actorSystem().registerOnTermination(leaveCluster);

        cluster.registerOnMemberRemoved( () -> {
            LOGGER.info("Exiting...");
            cluster.leave(cluster.selfAddress());
            actorSystem().registerOnTermination(() -> System.exit(0));
            actorSystem().terminate();
        });

        Runtime.getRuntime().addShutdownHook(new Thread(leaveCluster)
        );

        LOGGER.info("Step 3/3: Cluster connected to Woken.");

        return cluster;
    }

    @Bean
    public String wokenClusterHost() {
        return config.getString("clustering.ip");
    }

    @Bean
    public String wokenClusterName() {
        return config.getString("clustering.cluster.name");
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
    @DependsOn("wokenCluster")
    public ActorRef wokenMediator() {
        return DistributedPubSub.get(actorSystem()).mediator();
    }

}
