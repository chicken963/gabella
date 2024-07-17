package ru.verstache.gabella.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.verstache.gabella.PostgresTestContainerInitializer;
import ru.verstache.gabella.dto.PlayerDto;
import ru.verstache.gabella.dto.ServerDto;
import ru.verstache.gabella.model.Server;
import ru.verstache.gabella.model.stats.ServerStats;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(initializers = PostgresTestContainerInitializer.class)
public class ServerServerIntTest {

    @Autowired
    private ServerService sut;

    @Test
    void shouldFindAll() {
        Set<ServerDto> all = sut.findAll();
        assertThat(all).hasSize(3);
    }

    @Test
    void shouldFindById() {
        UUID id = UUID.fromString("387fad9a-f121-8a3c-7cbb-17bcfd2391d6");
        ServerDto server = sut.findDtoById(id);
        assertThat(server.name()).isEqualTo("rush_c");
    }

    @Test
    void shouldFindMostPopularServer() {
        List<ServerDto> mostPopularServers = sut.findMostPopularServers(2);

        assertThat(mostPopularServers).hasSize(2);
        assertThat(mostPopularServers.get(0).name()).isEqualTo("rush_b");
        assertThat(mostPopularServers.get(1).name()).isEqualTo("rush_a");
    }

    @Test
    void shouldGenerateNewServer_whenNameNotExistsYet() {
        final String nonExistingName = "non_existing_name";
        Server server = sut.findByName(nonExistingName);
        assertNotNull(server.getId());
        assertThat(server.getName()).isEqualTo(nonExistingName);
    }

    @Test
    void shouldPrepareServerStats() {
        final UUID serverId = UUID.fromString("db7b1d54-ff13-48d0-b6c7-06ba5507a305");
        ServerStats serversStats = sut.getServerStats(serverId);
        ServerStats expectedStats = ServerStats.builder()
                .name("rush_b")
                .createdAt(LocalDateTime.of(2024, 6, 28, 19, 46, 33, 514582000))
                .lastPlayedAt(LocalDateTime.of(2024, 6, 24, 22, 12, 41, 435000000))
                .totalPlayedMatches(3)
                .averageMatchesPerDay(1.5)
                .maxMatchesPerDay(2)
                .mostPlayedDay(LocalDate.of(2024, 6, 24))
                .bestPlayer(new PlayerDto("Morty"))
                .build();
        assertEquals(expectedStats, serversStats);
    }
}
