package de.quoss.activemq.artemis.send.message;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.Topic;

public class Consumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);
    
    public static void main(final String[] args) throws Exception {
        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://localhost:61616");
                JMSContext context = cf.createContext()) {
            Topic topic = context.createTopic("jms-ok");
            Message received;
            String body = null;
            while (body == null) {
                try (final JMSConsumer consumer = context.createSharedDurableConsumer(topic, "consumer0")) {
                    received = consumer.receive(5000L);
                }
                body = received == null ? null : received.getBody(String.class);
                LOGGER.info("Body: {}", body);
            }
        }
    }

}
