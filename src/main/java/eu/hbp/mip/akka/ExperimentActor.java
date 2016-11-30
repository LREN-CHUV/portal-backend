package eu.hbp.mip.akka;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;
import eu.hbp.mip.messages.external.QueryResult;

import java.util.UUID;

/**
 * Created by mirco on 30.11.16.
 */
public class ExperimentActor extends UntypedActor {

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
        }
        else {
            unhandled(message);
        }
    }
}
