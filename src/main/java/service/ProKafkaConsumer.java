package main.java.service;

import main.java.config.KafkaConfig;
import main.java.dto.ProEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ProKafkaConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(ProKafkaConsumer.class);

    @KafkaListener(topics = KafkaConfig.PLAYER_TOPIC, groupId = "pro-team-group", autoStartup = "${spring.kafka.enabled:true}")
    public void consumePlayerEvent(ProEvent event) {
        LOG.info("Received player event: {}", event);
        // Process the event (e.g., update a read model, notify another system, etc.)
    }
}
