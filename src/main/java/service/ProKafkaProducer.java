package main.java.service;

import main.java.dto.ProEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProKafkaProducer {

    private static final Logger LOG = LoggerFactory.getLogger(ProKafkaProducer.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendEvent(String topic, ProEvent event) {
        LOG.info("Sending event to topic {}: {}", topic, event);
        kafkaTemplate.send(topic, event);
    }
}
