package de.quoss.activemq.artemis.publish.subscribe;

import org.apache.activemq.artemis.api.core.QueueConfiguration;
import org.apache.activemq.artemis.api.core.RoutingType;
import org.apache.activemq.artemis.api.core.SimpleString;
import org.apache.activemq.artemis.api.core.client.ActiveMQClient;
import org.apache.activemq.artemis.api.core.client.ClientMessage;
import org.apache.activemq.artemis.api.core.client.ClientProducer;
import org.apache.activemq.artemis.api.core.client.ClientSession;
import org.apache.activemq.artemis.api.core.client.ClientSession.AddressQuery;
import org.apache.activemq.artemis.api.core.client.ClientSessionFactory;
import org.apache.activemq.artemis.api.core.client.ServerLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PubZeroSubZero {

	private static final Logger LOGGER = LoggerFactory.getLogger(PubZeroSubZero.class);
	
	private static final String ADDRESS_NAME = "pub-0";
	
	private static final String QUEUE_NAME = "sub-0";

	private static final String FQQ_NAME = "%s::%s".formatted(ADDRESS_NAME, QUEUE_NAME);
	
	public static void main(String[] args) throws Exception {
        final ServerLocator locator = ActiveMQClient.createServerLocator("tcp://localhost:61616");
        final ClientSessionFactory factory = locator.createSessionFactory();
        try (final ClientSession session = factory.createSession()) {
        	session.start();
        	final AddressQuery query = session.addressQuery(SimpleString.of(ADDRESS_NAME));
        	if (query.isExists()) {
				LOGGER.info("(main) Address {} exists.", ADDRESS_NAME);
        	} else {
        		session.createAddress(SimpleString.of(ADDRESS_NAME), RoutingType.MULTICAST, false);
        	}
        	if (query.getQueueNames().contains(SimpleString.of(QUEUE_NAME))) {
        		LOGGER.info("(main) Queue {} exists.", QUEUE_NAME);
        	} else {
        		final QueueConfiguration configuration = QueueConfiguration.of(QUEUE_NAME);
        		configuration.setAddress(ADDRESS_NAME);
        		session.createQueue(configuration);
        	}
        	try (final ClientProducer producer = session.createProducer(FQQ_NAME)) {
    			final ClientMessage message = session.createMessage(true);
    			message.getBodyBuffer().writeString("Hello");
    			producer.send(message);
				LOGGER.info("(main) Message {} sent.", message);
            	session.commit();
        	}
        }
	}

}
