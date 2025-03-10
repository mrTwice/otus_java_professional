package ru.otus.java.professional.yampolskiy.spring.homework17springjmsactivemq.services.active;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.ObjectMessage;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import ru.otus.java.professional.yampolskiy.spring.homework17springjmsactivemq.config.ActiveMqConfig;

import java.io.Serializable;

@Service
@RequiredArgsConstructor
public class ActiveMqListener {

    private final ObjectMapper objectMapper;

    @JmsListener(destination = ActiveMqConfig.DESTINATION_NAME
            , containerFactory = ActiveMqConfig.ACTIVE_MQ_JMS_LISTENER_CONTAINER_FACTORY)
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage)
                onTextMessage((TextMessage) message);
            else if (message instanceof ObjectMessage)
                onObjectMessage((ObjectMessage) message);
            else
                throw new IllegalArgumentException("Message Error");
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void onTextMessage(TextMessage message) throws JMSException {
        String msg = message.getText();
        System.out.format("[activemq: text] : %s\n", msg);
    }

    private void onObjectMessage(ObjectMessage message)
            throws JMSException, ClassNotFoundException, JsonProcessingException {
        String className = message.getStringProperty(ActiveMqConfig.CLASS_NAME);

        if (className == null)
            onSerializableObjectMessage(message);
        else
            onCustomObjectMessage(Class.forName(className), message);
    }

    private static void onSerializableObjectMessage(ObjectMessage message) throws JMSException {
        Serializable obj = message.getObject();
        System.out.format("[activemq: serializable] : %s\n", obj);
    }

    private void onCustomObjectMessage(Class<?> cls, ObjectMessage message)
            throws JMSException, JsonProcessingException {
        String json = String.valueOf(message.getObject());
        Object obj = objectMapper.readValue(json, cls);
        System.out.format("[activemq: %s] : %s\n", cls.getSimpleName(), obj);
    }
}
