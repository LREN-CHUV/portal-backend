package eu.hbp.mip.akka;

import akka.actor.AbstractActor;
import eu.hbp.mip.woken.messages.external.QueryResult;
import eu.hbp.mip.model.Experiment;
import eu.hbp.mip.repositories.ExperimentRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * Created by mirco on 30.11.16.
 */

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ExperimentActor extends AbstractActor {

    @Autowired
    private ExperimentRepository experimentRepository;

    private static final Logger LOGGER = Logger.getLogger(ExperimentActor.class);

    public Receive createReceive() {
        return receiveBuilder()
                .match(QueryResult.class, this::handleQueryResult)
                .matchAny(o -> LOGGER.info("received unknown message " + o))
                .build();
    }

    private void handleQueryResult(QueryResult queryResult) {
        LOGGER.info("ActorExperiment - onReceive method has been called");
        UUID uuid = UUID.fromString(this.getSelf().path().name());
        LOGGER.info("\n\nExperimentActor received response from woken for UUID: \n" + uuid.toString());
        Experiment experiment = experimentRepository.findOne(uuid);
        if (experiment == null) {
            LOGGER.error("Experiment with UUID=" + uuid + " not found in DB");
            getContext().stop(getSelf());
        } else {
            if (queryResult.error().nonEmpty()) {
                experiment.setHasServerError(true);
                experiment.setResult(queryResult.error().get());
            } else {
                experiment.setResult(queryResult.data().get());
            }
            experiment.setFinished(Date.from(queryResult.timestamp().toInstant()));
            experimentRepository.save(experiment);
            LOGGER.info("Experiment " + uuid + " updated (finished)");
            getContext().stop(getSelf());
        }
    }
}
