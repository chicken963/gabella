package ru.verstache.gabella;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresTestContainer {

    private static final PostgreSQLContainer<?> container;

    static {
        container = new PostgreSQLContainer<>("postgres:14.3")
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test");
        container.start();
    }

    public static PostgreSQLContainer<?> getInstance() {
        return container;
    }
}
