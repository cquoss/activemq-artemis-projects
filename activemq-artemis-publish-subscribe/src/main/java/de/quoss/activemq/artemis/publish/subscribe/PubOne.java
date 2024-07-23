package de.quoss.activemq.artemis.publish.subscribe;

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

import jakarta.jms.Message;

public class PubOne {

	private static final Logger LOGGER = LoggerFactory.getLogger(PubOne.class);
	
	private static final String ADDRESS_NAME = "pub-1";
	
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
        	try (final ClientProducer producer = session.createProducer(ADDRESS_NAME)) {
    			final ClientMessage message = session.createMessage(true);
    			message.getBodyBuffer().writeNullableSimpleString(SimpleString.of("Hello"));
    			message.putStringProperty("foo", "bar");
    			message.setType(org.apache.activemq.artemis.api.core.Message.TEXT_TYPE);
    			message.reencode();
    			producer.send(message);
				LOGGER.info("(main) Message {} sent.", message);
            	session.commit();
        	}
        }
	}

}
