package eu.hbp.mip.akka;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import eu.hbp.mip.messages.external.Methods;

/**
 * Created by mirco on 30.11.16.
 */
public class SimpleActor extends UntypedActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Methods) {
            Methods methods = (Methods) message;
            log.info("received methods");
        }
        else {
            unhandled(message);
        }
    }
}
