package de.quoss.activemq.artemis.send.message;

import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;

public class Broker {

    private static final EmbeddedActiveMQ EMBEDDED = new EmbeddedActiveMQ();
    
    public static void main(final String[] args) throws Exception {
        EMBEDDED.start();
        Thread.sleep(300000L);
        EMBEDDED.stop();
    }

}
