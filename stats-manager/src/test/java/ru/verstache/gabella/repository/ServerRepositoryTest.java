package ru.verstache.gabella.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.verstache.gabella.PostgresTestContainerInitializer;
import ru.verstache.gabella.model.Server;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ServerRepositoryTest extends PostgresTestContainerInitializer {

    @Autowired
    private ServerRepository serverRepository;

    @Test
    void shouldFindByName() {
        Optional<Server> serverOpt = serverRepository.findByName("rush_a");
        assertThat(serverOpt).isPresent();
    }
}
