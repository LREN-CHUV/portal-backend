package eu.hbp.mip.akka;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import eu.hbp.mip.messages.external.QueryError;
import eu.hbp.mip.messages.external.QueryResult;
import eu.hbp.mip.model.Experiment;
import eu.hbp.mip.repositories.ExperimentRepository;
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

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);


    @Override
    public void onReceive(Object message) {
        log.info("ActorExperiment - onReceive method has been called");
        UUID uuid = UUID.fromString(this.getSelf().path().name());
        log.info("\n\nExperimentActor received response from woken for UUID: \n"+uuid.toString());
        if (message instanceof QueryResult) {
            QueryResult queryResult = (QueryResult) message;
            Experiment experiment = experimentRepository.findOne(uuid);
            if(experiment == null)
            {
                log.error("Experiment with UUID="+uuid+" not found in DB");
                getContext().stop(getSelf());
                return;
            }
            experiment.setResult(queryResult.data().get());
            experiment.setFinished(new Date());
            experimentRepository.save(experiment);
            log.info("Experiment "+ uuid +" updated (finished)");
            getContext().stop(getSelf());
        }

        else if (message instanceof QueryError) {
            QueryError queryError = (QueryError) message;
            log.warning("received query error");
            Experiment experiment = experimentRepository.findOne(uuid);
            if(experiment == null)
            {
                log.error("Experiment with UUID="+uuid+" not found in DB");
                getContext().stop(getSelf());
                return;
            }
            experiment.setHasServerError(true);
            experiment.setResult(queryError.message());
            experimentRepository.save(experiment);
            experiment.setFinished(new Date());
            experimentRepository.save(experiment);
            log.info("Experiment "+ uuid +" updated (finished)");
            getContext().stop(getSelf());
        }

        else {
            unhandled(message);
        }
    }
}
