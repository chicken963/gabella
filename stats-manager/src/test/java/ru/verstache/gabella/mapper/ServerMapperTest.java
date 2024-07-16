package ru.verstache.gabella.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.verstache.gabella.PostgresTestContainerInitializer;
import ru.verstache.gabella.dto.ServerDto;
import ru.verstache.gabella.model.Server;
import ru.verstache.gabella.repository.ServerRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(initializers = PostgresTestContainerInitializer.class)
public class ServerMapperTest {

    @Autowired
    private ServerMapper sut;

    @Autowired
    private ServerRepository serverRepository;

    @Test
    void shouldGenerateNewServer() {
        final String newName = "some_new_name";
        ServerDto serverDto = new ServerDto(UUID.randomUUID(), newName, LocalDateTime.now());

        sut.toModel(serverDto);

        Optional<Server> serverFromDb = serverRepository.findByName(newName);
        assertThat(serverFromDb).isPresent();
    }
}
