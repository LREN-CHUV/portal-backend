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

    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ActorSystem actorSystem;

    @Autowired
    private String wokenReceptionistPath;

    @Value("#{'${akka.woken.path:/user/entrypoint}'}")
    private String wokenPath;

    private ActorRef wokenClient;

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unchecked")
    protected <A, B> B askWoken(A message, int waitInSeconds) throws Exception {
        LOGGER.info("Akka is trying to reach remote " + wokenPath);

        ClusterClient.Send queryMessage = new ClusterClient.Send(wokenPath, message, true);
        Timeout timeout = new Timeout(Duration.create(waitInSeconds, "seconds"));

        Future<Object> future = Patterns.ask(wokenClient, queryMessage, timeout);

        return (B) Await.result(future, timeout.duration());
    }

    protected <A, B> ResponseEntity requestWoken(A message, int waitInSeconds, Function<B, ResponseEntity> handleResponse) {
        try {
            B result = askWoken(message, waitInSeconds);
            return handleResponse.apply(result);
        } catch (Exception e) {
            final String msg = "Cannot receive result from woken: " + e.getMessage();
            LOGGER.error(msg, e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(msg);
        }
    }

    protected <A extends Query> ResponseEntity askWokenQuery(A query, int waitInSeconds, Function<QueryResult, ResponseEntity> handleResponse) {
        try {
            QueryResult result = askWoken(query, waitInSeconds);
            return handleResponse.apply(result);
        } catch (Exception e) {
            final String msg = "Cannot receive algorithm result from woken: " + e.getMessage();
            LOGGER.error(msg, e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(msg);
        }
    }

    protected <A extends Query> Future<Object> sendWokenQuery(A query, int timeout) {
        LOGGER.info("Akka is trying to reach remote " + wokenPath);

        ClusterClient.Send queryMessage = new ClusterClient.Send(wokenPath, query, true);

        return Patterns.ask(wokenClient, queryMessage, timeout);
    }

    protected ExecutionContext getExecutor() {
        return actorSystem.dispatcher();
    }
}
