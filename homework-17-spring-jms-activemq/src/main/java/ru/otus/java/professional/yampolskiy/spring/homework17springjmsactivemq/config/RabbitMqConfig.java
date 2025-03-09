package ru.otus.java.professional.yampolskiy.spring.homework17springjmsactivemq.config;

import com.rabbitmq.jms.admin.RMQConnectionFactory;
import jakarta.jms.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
public class RabbitMqConfig {

    public static final String RABBIT_MQ_JMS_TEMPLATE = "rabbitMqJmsTemplate";
    public static final String RABBIT_MQ_JMS_LISTENER_CONTAINER_FACTORY = "rabbitMqJmsListenerContainerFactory";

    private static final String RABBIT_MQ_CONNECTION_FACTORY = "rabbitMqConnectionFactory";

    public static final String DESTINATION_NAME = "foo";
    public static final String CLASS_NAME = "className";

    @Bean(RABBIT_MQ_CONNECTION_FACTORY)
    public ConnectionFactory connectionFactory() {
        RMQConnectionFactory connectionFactory = new RMQConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("user");
        connectionFactory.setPassword("rabbit");
        return connectionFactory;
    }

    @Bean(RABBIT_MQ_JMS_TEMPLATE)
    public JmsTemplate jmsTemplate(@Qualifier(RABBIT_MQ_CONNECTION_FACTORY) ConnectionFactory cachingConnectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(cachingConnectionFactory);
        jmsTemplate.setReceiveTimeout(TimeUnit.SECONDS.toMillis(10));
        return jmsTemplate;
    }

    @Bean(RABBIT_MQ_JMS_LISTENER_CONTAINER_FACTORY)
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(
            @Qualifier(RABBIT_MQ_CONNECTION_FACTORY) ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }

}
