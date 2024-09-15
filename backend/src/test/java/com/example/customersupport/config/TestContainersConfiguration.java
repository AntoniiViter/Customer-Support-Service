package com.example.customersupport.config;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class TestContainersConfiguration {

    @Bean
    @SuppressWarnings("resource")
    @ServiceConnection("postgres")
    public PostgreSQLContainer<?> postgreSQLContainer() {
        return new PostgreSQLContainer<>("postgres:latest").withReuse(true);
    }

    @Bean
    @SuppressWarnings("resource")
    @ServiceConnection("mongo")
    public MongoDBContainer mongoDBContainer() {
        return new MongoDBContainer("mongo:latest").withReuse(true);
    }

    @Bean
    @SuppressWarnings("resource")
    @ServiceConnection("redis")
    public RedisContainer redisContainer() {
        return new RedisContainer(DockerImageName.parse("redis:latest")).withReuse(true);
    }

}
