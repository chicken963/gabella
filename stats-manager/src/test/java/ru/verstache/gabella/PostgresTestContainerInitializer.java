package ru.verstache.gabella;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.HashMap;
import java.util.Map;

public class PostgresTestContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        PostgreSQLContainer<?> postgresContainer = PostgresTestContainer.getInstance();

        Map<String, Object> properties = new HashMap<>();
        properties.put("spring.datasource.url", postgresContainer.getJdbcUrl());
        properties.put("spring.datasource.username", postgresContainer.getUsername());
        properties.put("spring.datasource.password", postgresContainer.getPassword());

        MutablePropertySources propertySources = applicationContext.getEnvironment().getPropertySources();
        propertySources.addFirst(new MapPropertySource("testcontainers", properties));
    }
}
