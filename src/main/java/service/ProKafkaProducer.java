package main.java.service;

import main.java.dto.ProEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true")
public class ProKafkaProducer {

    private static final Logger LOG = LoggerFactory.getLogger(ProKafkaProducer.class);

    @Autowired(required = false)
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendEvent(String topic, ProEvent event) {
        if (kafkaTemplate != null) {
            LOG.info("Sending event to topic {}: {}", topic, event);
            kafkaTemplate.send(topic, event);
        } else {
            LOG.debug("Kafka is disabled. Skipping event sending for topic {}: {}", topic, event);
        }
    }
}
