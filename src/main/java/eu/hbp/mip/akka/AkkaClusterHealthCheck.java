package eu.hbp.mip.akka;

import akka.actor.ActorRef;
import akka.cluster.Cluster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;


@Component
public class AkkaClusterHealthCheck implements HealthIndicator {

    @Autowired
    private ActorRef wokenMediator;

    @Autowired
    private Cluster cluster;

    @Override
    public Health health() {
        if (cluster.state().getLeader() == null) {
            return Health.down().withDetail("Error", "No leader in the cluster").build();
        } else if (!cluster.state().allRoles().contains("woken")) {
            return Health.down().withDetail("Error", "Woken server cannot be seen in the cluster").build();
        }
        return Health.up().build();
    }

}