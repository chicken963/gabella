package ru.verstache.gabella.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.verstache.gabella.PostgresTestContainerInitializer;
import ru.verstache.gabella.model.Server;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(initializers = PostgresTestContainerInitializer.class)
public class ServerRepositoryTest {

    @Autowired
    private ServerRepository serverRepository;

    @Test
    void shouldFindByName() {
        Optional<Server> serverOpt = serverRepository.findByName("rush_a");
        assertThat(serverOpt).isPresent();
    }
}
