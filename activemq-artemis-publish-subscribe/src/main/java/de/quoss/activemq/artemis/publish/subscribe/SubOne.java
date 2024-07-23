package de.quoss.activemq.artemis.publish.subscribe;

import org.apache.activemq.artemis.api.core.QueueConfiguration;
import org.apache.activemq.artemis.api.core.RoutingType;
import org.apache.activemq.artemis.api.core.SimpleString;
import org.apache.activemq.artemis.api.core.client.ActiveMQClient;
import org.apache.activemq.artemis.api.core.client.ClientConsumer;
import org.apache.activemq.artemis.api.core.client.ClientMessage;
import org.apache.activemq.artemis.api.core.client.ClientSession;
import org.apache.activemq.artemis.api.core.client.ClientSession.AddressQuery;
import org.bouncycastle.crypto.ec.ECNewPublicKeyTransform;
import org.apache.activemq.artemis.api.core.client.ClientSessionFactory;
import org.apache.activemq.artemis.api.core.client.ServerLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubOne {

	private static final Logger LOGGER = LoggerFactory.getLogger(SubOne.class);
	
	private static final String ADDRESS_NAME = "pub-1";
	
	private static final String QUEUE_NAME = "pub-1-sub-1";
	
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
        	try (final ClientConsumer consumer = session.createConsumer(QUEUE_NAME)) {
        		while (true) {
        			final ClientMessage message = consumer.receive(10000L);
        			if (message == null) {
        				LOGGER.info("(main) No message received.");
        			} else {
        				LOGGER.info("(main) Message {} received.", message);
        				LOGGER.info("(main) Message Data Buffer Capacity: {}", message.getDataBuffer().capacity());
        				LOGGER.info("(main) Message Data Buffer Readable Bytes: {}", message.getDataBuffer().readableBytes());
        				LOGGER.info("(main) Message Data Buffer Content: {}", message.getDataBuffer().readNullableSimpleString());
        				message.acknowledge();
        			}
                	session.commit();
        		}
        	}
        }
	}

}
