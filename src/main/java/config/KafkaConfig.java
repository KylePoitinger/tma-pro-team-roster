package main.java.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true", matchIfMissing = true)
public class KafkaConfig {

    public static final String PLAYER_TOPIC = "pro-player-events";

    @Bean
    public NewTopic playerTopic() {
        return TopicBuilder.name(PLAYER_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
