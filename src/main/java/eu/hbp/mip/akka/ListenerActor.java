package eu.hbp.mip.akka;

import akka.actor.UntypedActor;
import org.springframework.context.annotation.Scope;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by mirco on 24.10.16.
 */

@Named("ListenerActor")
@Scope("prototype")
public class ListenerActor extends UntypedActor {

    public static class AlgoQuery {
        private final String query;
        public AlgoQuery(String query) {
            this.query = query;
        }
        public String getQuery() {
            return query;
        }
    }
    public static class AlgoResult {
        private final String result;
        public AlgoResult(String result) {
            this.result = result;
        }
        public String getResult() {
            return result;
        }
    }
    final ListeningService listeningService;
    @Inject
    public ListenerActor(@Named("ListeningService") ListeningService listeningService) {
        this.listeningService = listeningService;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof AlgoResult) {
            AlgoResult algoResult = (AlgoResult) message;
            listeningService.listen(algoResult.getResult());
        }
        else {
            unhandled(message);
        }
    }

}