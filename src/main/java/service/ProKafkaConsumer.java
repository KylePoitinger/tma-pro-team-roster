package main.java.service;

import main.java.config.KafkaConfig;
import main.java.dto.ProEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true")
public class ProKafkaConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(ProKafkaConsumer.class);

    @Autowired
    private ProPlayerService proPlayerService;

    @KafkaListener(topics = KafkaConfig.PLAYER_TOPIC, groupId = "pro-team-group", autoStartup = "${spring.kafka.enabled:true}")
    public void consumePlayerEvent(ProEvent event) {
        LOG.debug("Received player event: {}", event);
        
        if ("PLAYER_TRADE_REQUESTED".equals(event.eventType())) {
            handleTradeRequested(event);
        }
    }

    private void handleTradeRequested(ProEvent event) {
        try {
            Map<String, Object> payload = (Map<String, Object>) event.payload();
            long playerId = ((Number) payload.get("playerId")).longValue();
            long teamId = ((Number) payload.get("teamId")).longValue();
            
            LOG.info("Processing trade request via Kafka for player {} to team {}", playerId, teamId);
            proPlayerService.tradePlayer(playerId, teamId);
        } catch (Exception e) {
            LOG.error("Failed to process trade request from Kafka: {}", e.getMessage(), e);
        }
    }
}
