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
        log.info("\n\n***** ExperimentActor received response from woken\n");
        UUID uuid = UUID.fromString(this.getSelf().path().name());
        if (message instanceof QueryResult) {
            QueryResult queryResult = (QueryResult) message;
            log.info("received query result for : " + uuid.toString());
            Experiment experiment = experimentRepository.findOne(uuid);
            if(experiment == null)
            {
                log.error("Experiment with UUID="+uuid+" not found in DB");
                return;
            }
            experiment.setResult(queryResult.data().get());
            experiment.setFinished(new Date());
            experimentRepository.save(experiment);
            log.info("Experiment "+ uuid +" updated (finished)");
        }

        else if (message instanceof QueryError) {
            QueryError queryError = (QueryError) message;
            log.warning("received query error");
            Experiment experiment = experimentRepository.findOne(uuid);
            if(experiment == null)
            {
                log.error("Experiment with UUID="+uuid+" not found in DB");
                return;
            }
            experiment.setHasServerError(true);
            experiment.setResult(queryError.message());
            experimentRepository.save(experiment);
            experiment.setFinished(new Date());
            experimentRepository.save(experiment);
            log.info("Experiment "+ uuid +" updated (finished)");
        }

        else {
            unhandled(message);
        }
    }
}
