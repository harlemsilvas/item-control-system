package br.com.harlemsilvas.itemcontrol.api;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Configuração base para testes de integração com Testcontainers.
 */
@TestConfiguration
public class TestContainersConfiguration {

    private static final MongoDBContainer mongoDBContainer;

    static {
        mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:7.0"))
                .withExposedPorts(27017);
        mongoDBContainer.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.data.mongodb.database", () -> "test_db");
    }

    @Bean
    public MongoDBContainer mongoDBContainer() {
        return mongoDBContainer;
    }
}
