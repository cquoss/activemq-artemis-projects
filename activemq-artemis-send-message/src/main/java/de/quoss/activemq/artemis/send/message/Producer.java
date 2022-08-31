package de.quoss.activemq.artemis.send.message;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.TextMessage;
import javax.jms.Topic;

public class Producer {

    public static void main(final String[] args) throws Exception {
        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://localhost:61616");
                JMSContext context = cf.createContext()) {
            Topic topic = context.createTopic("jms-ok::consumer0");
            JMSProducer producer = context.createProducer();
            final TextMessage message = context.createTextMessage();
            message.setText("test");
            producer.send(topic, message);
        }
    }

}
