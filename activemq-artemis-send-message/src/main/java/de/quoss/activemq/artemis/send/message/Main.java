package de.quoss.activemq.artemis.send.message;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.TextMessage;

public class Main {

    public static void main(final String[] args) throws Exception {
        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
                JMSContext context = cf.createContext()) {
            Destination d = context.createTopic("jms-ok");
            JMSProducer producer = context.createProducer();
            TextMessage message = context.createTextMessage();
            message.setText("test");
            producer.send(d, message);
        }
    }

}
