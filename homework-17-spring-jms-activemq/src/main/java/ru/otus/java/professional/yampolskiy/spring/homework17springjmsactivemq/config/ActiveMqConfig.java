package ru.otus.java.professional.yampolskiy.spring.homework17springjmsactivemq.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.jms.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
public class ActiveMqConfig {
    public static final String ACTIVE_MQ_JMS_TEMPLATE = "activeMqJmsTemplate";
    public static final String ACTIVE_MQ_JMS_LISTENER_CONTAINER_FACTORY = "activeMqJmsListenerContainerFactory";

    private static final String ACTIVE_MQ_CONNECTION_FACTORY = "activeMqConnectionFactory";

    public static final String DESTINATION_NAME = "foo";
    public static final String CLASS_NAME = "className";

    @Bean(ACTIVE_MQ_CONNECTION_FACTORY)
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL("tcp://localhost:61616");
        connectionFactory.setUserName("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setTrustAllPackages(true);
        return connectionFactory;
    }

    @Bean(ACTIVE_MQ_JMS_TEMPLATE)
    public JmsTemplate jmsTemplate(@Qualifier(ACTIVE_MQ_CONNECTION_FACTORY) ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setReceiveTimeout(TimeUnit.SECONDS.toMillis(10));
        return jmsTemplate;
    }

    @Bean(ACTIVE_MQ_JMS_LISTENER_CONTAINER_FACTORY)
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(
            @Qualifier(ACTIVE_MQ_CONNECTION_FACTORY) ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }
}
