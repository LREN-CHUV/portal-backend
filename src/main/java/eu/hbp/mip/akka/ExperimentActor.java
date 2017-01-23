package eu.hbp.mip.akka;

import akka.actor.UntypedActor;
import eu.hbp.mip.messages.external.QueryError;
import eu.hbp.mip.messages.external.QueryResult;
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
public class ExperimentActor extends UntypedActor {

    @Autowired
    private ExperimentRepository experimentRepository;

    private static final Logger LOGGER = Logger.getLogger(ExperimentActor.class);


    @Override
    public void onReceive(Object message) {
        LOGGER.info("ActorExperiment - onReceive method has been called");
        UUID uuid = UUID.fromString(this.getSelf().path().name());
        LOGGER.info("\n\nExperimentActor received response from woken for UUID: \n"+uuid.toString());
        if (message instanceof QueryResult) {
            QueryResult queryResult = (QueryResult) message;
            Experiment experiment = experimentRepository.findOne(uuid);
            if(experiment == null)
            {
                LOGGER.error("Experiment with UUID="+uuid+" not found in DB");
                getContext().stop(getSelf());
                return;
            }
            experiment.setResult(queryResult.data().get());
            experiment.setFinished(new Date());
            experimentRepository.save(experiment);
            LOGGER.info("Experiment "+ uuid +" updated (finished)");
            getContext().stop(getSelf());
        }

        else if (message instanceof QueryError) {
            QueryError queryError = (QueryError) message;
            LOGGER.warn("received query error");
            Experiment experiment = experimentRepository.findOne(uuid);
            if(experiment == null)
            {
                LOGGER.error("Experiment with UUID="+uuid+" not found in DB");
                getContext().stop(getSelf());
                return;
            }
            experiment.setHasServerError(true);
            experiment.setResult(queryError.message());
            experimentRepository.save(experiment);
            experiment.setFinished(new Date());
            experimentRepository.save(experiment);
            LOGGER.info("Experiment "+ uuid +" updated (finished)");
            getContext().stop(getSelf());
        }

        else {
            unhandled(message);
        }
    }
}
