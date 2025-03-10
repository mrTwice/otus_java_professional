package ru.otus.java.professional.yampolskiy.spring.homework17springjmsactivemq.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.java.professional.yampolskiy.spring.homework17springjmsactivemq.dto.OrderDto;
import ru.otus.java.professional.yampolskiy.spring.homework17springjmsactivemq.dto.StudentDto;
import ru.otus.java.professional.yampolskiy.spring.homework17springjmsactivemq.services.active.ActiveMqProducer;

@RestController
@RequestMapping("/api/v1/activemq")
@RequiredArgsConstructor
public class ActiveMqController {
    private final ActiveMqProducer activeMqProducer;

    @PostMapping("/text")
    public ResponseEntity<String> sendTextMessage(@RequestBody String text) {
        activeMqProducer.sendAsTextMessage(text);
        return ResponseEntity.ok("Text message sent successfully with ActiveMQ");
    }

    @PostMapping("/serializable")
    public ResponseEntity<String> sendSerializableMessage(@RequestBody StudentDto studentDto) {
        activeMqProducer.sendAsSerializableMessage(studentDto);
        return ResponseEntity.ok("Serializable message sent successfully with ActiveMQ");
    }

    @PostMapping("/objectMessage")
    public ResponseEntity<String> sendMessageCreator(@RequestBody OrderDto orderDto) {
        activeMqProducer.sendAsMessageCreator(orderDto);
        return ResponseEntity.ok("Object message sent successfully with ActiveMQ");
    }
}
