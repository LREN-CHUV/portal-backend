package eu.hbp.mip.akka;

import akka.actor.UntypedActor;
import eu.hbp.mip.messages.external.QueryResult;
import org.springframework.context.annotation.Scope;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by mirco on 24.10.16.
 */

@Named("ListenerActor")
@Scope("prototype")
public class ListenerActor extends UntypedActor {

    final ListeningService listeningService;
    @Inject
    public ListenerActor(@Named("ListeningService") ListeningService listeningService) {
        this.listeningService = listeningService;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof QueryResult) {
            QueryResult queryResult = (QueryResult) message;
            listeningService.listen(queryResult.data().get());
        }
        else {
            unhandled(message);
        }
    }

}