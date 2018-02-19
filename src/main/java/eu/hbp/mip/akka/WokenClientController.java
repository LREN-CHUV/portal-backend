package eu.hbp.mip.akka;

import akka.actor.ActorPath;
import akka.actor.ActorPaths;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.cluster.client.ClusterClient;
import akka.cluster.client.ClusterClientSettings;
import akka.pattern.Patterns;
import akka.util.Timeout;
import ch.chuv.lren.woken.messages.query.Query;
import ch.chuv.lren.woken.messages.query.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import scala.concurrent.Await;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

/**
 * Base class for controllers using Woken services
 */
public abstract class WokenClientController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ActorSystem actorSystem;

    @Autowired
    private String wokenReceptionistPath;

    @Value("#{'${akka.woken.path:/user/entrypoint}'}")
    private String wokenPath;

    private ActorRef wokenClient;

    @PostConstruct
    public void initClusterClient() {
        LOGGER.info("Start Woken client " + wokenReceptionistPath);
        wokenClient = actorSystem.actorOf(ClusterClient.props(
                ClusterClientSettings.create(actorSystem).withInitialContacts(initialContacts())),
                "client-" + getClass().getSimpleName());
    }

    private Set<ActorPath> initialContacts () {
        return Collections.singleton(ActorPaths.fromString(wokenReceptionistPath));
    }

    protected <A, B> ResponseEntity askWoken(A message, int waitInSeconds, Function<B, ResponseEntity> handleResponse) {
        LOGGER.info("Akka is trying to reach remote " + wokenPath);

        ClusterClient.Send queryMessage = new ClusterClient.Send(wokenPath, message, true);
        Timeout timeout = new Timeout(Duration.create(waitInSeconds, "seconds"));

        Future<Object> future = Patterns.ask(wokenClient, queryMessage, timeout);

        B result;
        try {
            result = (B) Await.result(future, timeout.duration());
        } catch (Exception e) {
            LOGGER.error("Cannot receive algorithm result from woken: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }

        return handleResponse.apply(result);
    }


    protected <A extends Query> ResponseEntity askWokenQuery(A query, int waitInSeconds, Function<QueryResult, ResponseEntity> handleResponse) {
        LOGGER.info("Akka is trying to reach remote " + wokenPath);

        ClusterClient.Send queryMessage = new ClusterClient.Send(wokenPath, query, true);
        Timeout timeout = new Timeout(Duration.create(waitInSeconds, "seconds"));

        Future<Object> future = Patterns.ask(wokenClient, queryMessage, timeout);

        QueryResult result;
        try {
            result = (QueryResult) Await.result(future, timeout.duration());
        } catch (Exception e) {
            LOGGER.error("Cannot receive algorithm result from woken: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }

        return handleResponse.apply(result);
    }

    protected <A extends Query> Future<Object> sendWokenQuery(A query, int timeout) {
        LOGGER.info("Akka is trying to reach remote " + wokenPath);

        ClusterClient.Send queryMessage = new ClusterClient.Send(wokenPath, query, true);

        return Patterns.ask(wokenClient, queryMessage, timeout);
    }

    protected ActorRef createActor(String actorBeanName, String actorName) {
        return actorSystem.actorOf(SpringExtension.SPRING_EXTENSION_PROVIDER.get(actorSystem)
                .props(actorBeanName), actorName);
    }

    protected ExecutionContext getExecutor() {
        return actorSystem.dispatcher();
    }
}
