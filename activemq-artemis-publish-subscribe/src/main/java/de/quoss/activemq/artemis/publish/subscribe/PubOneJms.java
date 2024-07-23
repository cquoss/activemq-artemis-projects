package de.quoss.activemq.artemis.publish.subscribe;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import jakarta.jms.Destination;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSProducer;
import jakarta.jms.TextMessage;

public class PubOneJms {

	private static final ActiveMQConnectionFactory FACTORY = new ActiveMQConnectionFactory("tcp://localhost:61616");
	
	private static final String ADDRESS_NAME = "pub-1";
	
	public static void main(final String... args) throws Exception {
		try (final JMSContext context = FACTORY.createContext()) {
            final JMSProducer producer = context.createProducer();
            final Destination destination = context.createTopic(ADDRESS_NAME);
            final TextMessage message = context.createTextMessage();
            message.setStringProperty("foo", "bar");
            message.setText("Hello");
			producer.send(destination, message);
			context.commit();
		}
	}
	
}
