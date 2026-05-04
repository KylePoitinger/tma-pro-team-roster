package main.java.integration;

import main.java.dto.ProEvent;
import main.java.entity.ProPlayerEntity;
import main.java.service.ProKafkaConsumer;
import main.java.service.ProPlayerService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeastOnce;

@EmbeddedKafka(partitions = 1, topics = { "pro-player-events" })
@SpringBootTest(properties = {
    "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
    "spring.kafka.enabled=true",
    "spring.kafka.admin.auto-create=true"
}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class KafkaIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ProPlayerService proPlayerService;

    @SpyBean
    private ProKafkaConsumer proKafkaConsumer;

    @Test
    public void testPlayerEventFlow() throws Exception {
        // Arrange
        ProPlayerEntity player = new ProPlayerEntity();
        player.setPlayerId(999L);
        player.setName("Kafka Test Player");
        player.setPosition("Forward");

        // Act
        proPlayerService.createProPlayer(player);

        // Assert - Use Awaitility for proper async verification
        await()
            .timeout(90, TimeUnit.SECONDS)
            .pollInterval(1, TimeUnit.SECONDS)
            .untilAsserted(() -> verify(proKafkaConsumer, atLeastOnce()).consumePlayerEvent(any(ProEvent.class)));
    }
}
