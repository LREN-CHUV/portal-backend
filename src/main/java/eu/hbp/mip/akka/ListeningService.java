package eu.hbp.mip.akka;

import javax.inject.Named;

/**
 * Created by mirco on 24.10.16.
 */

@Named("ListeningService")
public class ListeningService {
    public void listen(String result) {
        System.out.println(result);
    }
}