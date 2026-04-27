package main.java.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.core.env.Environment;

@SpringBootTest
@ActiveProfiles("test")
public class EnvDumpTest {
    @Autowired
    private Environment env;

    @Test
    public void dumpEnv() {
        System.out.println("[DEBUG_LOG] spring.boot.test.web-environment: " + env.getProperty("spring.boot.test.web-environment"));
        System.out.println("[DEBUG_LOG] TEST_ENVIRONMENT: " + env.getProperty("TEST_ENVIRONMENT"));
    }
}
