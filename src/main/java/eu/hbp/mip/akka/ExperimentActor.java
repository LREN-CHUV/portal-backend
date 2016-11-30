package eu.hbp.mip.akka;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;
import eu.hbp.mip.messages.external.QueryError;
import eu.hbp.mip.messages.external.QueryResult;
import eu.hbp.mip.model.Experiment;
import eu.hbp.mip.repositories.ExperimentRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.UUID;

/**
 * Created by mirco on 30.11.16.
 */
public class ExperimentActor extends UntypedActor {

    @Autowired
    private ExperimentRepository experimentRepository;

    public static Props props(final UUID expUUID) {
        return Props.create(new Creator<ExperimentActor>() {
            private static final long serialVersionUID = 1L;

            @Override
            public ExperimentActor create() throws Exception {
                return new ExperimentActor(expUUID);
            }
        });
    }

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private final UUID expUUID;

    private ExperimentActor(UUID expUUID) {
        this.expUUID = expUUID;
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof QueryResult) {
            QueryResult queryResult = (QueryResult) message;
            log.info("received query result for : " + expUUID.toString());
            Experiment experiment = experimentRepository.findOne(expUUID);
            if(experiment == null)
            {
                log.error("Experiment with UUID="+expUUID+" not found in DB");
                return;
            }
            experiment.setResult(queryResult.data().get());
            experiment.setFinished(new Date());
            experimentRepository.save(experiment);
            log.info("Experiment "+ expUUID +" updated (finished)");
        }

        else if (message instanceof QueryError) {
            QueryError queryError = (QueryError) message;
            log.warning("received query error");
            Experiment experiment = experimentRepository.findOne(expUUID);
            if(experiment == null)
            {
                log.error("Experiment with UUID="+expUUID+" not found in DB");
                return;
            }
            experiment.setHasServerError(true);
            experiment.setResult(queryError.message());
            experimentRepository.save(experiment);
            experiment.setFinished(new Date());
            experimentRepository.save(experiment);
            log.info("Experiment "+ expUUID +" updated (finished)");
        }

        else {
            unhandled(message);
        }
    }
}
