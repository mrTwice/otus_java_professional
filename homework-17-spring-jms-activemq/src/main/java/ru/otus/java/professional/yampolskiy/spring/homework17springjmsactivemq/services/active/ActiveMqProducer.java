package ru.otus.java.professional.yampolskiy.spring.homework17springjmsactivemq.services.active;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.ObjectMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import ru.otus.java.professional.yampolskiy.spring.homework17springjmsactivemq.config.ActiveMqConfig;
import ru.otus.java.professional.yampolskiy.spring.homework17springjmsactivemq.dto.OrderDto;
import ru.otus.java.professional.yampolskiy.spring.homework17springjmsactivemq.dto.StudentDto;

@Slf4j
@Service
public class ActiveMqProducer {
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    public ActiveMqProducer(@Qualifier(ActiveMqConfig.ACTIVE_MQ_JMS_TEMPLATE) JmsTemplate jmsTemplate,
                            ObjectMapper objectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendAsTextMessage(String text) {
        jmsTemplate.convertAndSend(ActiveMqConfig.DESTINATION_NAME, text);
    }

    public void sendAsSerializableMessage(StudentDto studentDto) {
        jmsTemplate.convertAndSend(ActiveMqConfig.DESTINATION_NAME, studentDto);
    }

    public void sendAsMessageCreator(OrderDto order) {
        jmsTemplate.send(ActiveMqConfig.DESTINATION_NAME, session -> {
            try {
                ObjectMessage objectMessage = session.createObjectMessage();
                objectMessage.setStringProperty(ActiveMqConfig.CLASS_NAME, OrderDto.class.getName());
                objectMessage.setObject(objectMapper.writeValueAsString(order));
                return objectMessage;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
