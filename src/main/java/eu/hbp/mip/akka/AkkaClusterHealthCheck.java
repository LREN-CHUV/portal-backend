package eu.hbp.mip.akka;

import akka.cluster.Cluster;
import ch.chuv.lren.woken.messages.Ping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import scala.Option;


@Component
public class AkkaClusterHealthCheck extends WokenClientController implements HealthIndicator {

    @Autowired
    private Cluster wokenCluster;

    @Override
    public Health health() {
        if (wokenCluster.state().getLeader() == null) {
            return Health.down().withDetail("Error", "No leader in the cluster").build();
        } else if (!wokenCluster.state().allRoles().contains("woken")) {
            return Health.down().withDetail("Error", "Woken server cannot be seen in the cluster").build();
        }
        try {
            askWoken(new Ping(Option.apply("woken")), 5);
            return Health.up().build();
        } catch (Exception e) {
            return Health.down().withDetail("Error", "Cannot reach Woken: " + e.toString()).build();
        }
    }

}
