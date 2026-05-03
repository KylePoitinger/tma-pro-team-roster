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

@SpringBootTest
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@Disabled("EmbeddedKafka connectivity issues in current build environment. Kafka producer/consumer tested indirectly through ProPlayerService. Re-enable with standalone broker or Docker.")
public class KafkaIntegrationTest {

    @Autowired
    private ProPlayerService proPlayerService;

    @SpyBean
    private ProKafkaConsumer proKafkaConsumer;

    @Test
    public void testPlayerEventFlow() throws Exception {
        // Arrange
        ProPlayerEntity player = new ProPlayerEntity();
        player.playerId = 999L;
        player.name = "Kafka Test Player";
        player.position = "Forward";

        // Act
        proPlayerService.createProPlayer(player);

        // Assert - Use Awaitility for proper async verification
        await()
            .timeout(60, TimeUnit.SECONDS)
            .pollInterval(500, TimeUnit.MILLISECONDS)
            .untilAsserted(() -> verify(proKafkaConsumer).consumePlayerEvent(any(ProEvent.class)));
    }
}
