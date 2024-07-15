package ru.verstache.gabella.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.verstache.gabella.PostgresTestContainerInitializer;
import ru.verstache.gabella.dto.PlayerDto;
import ru.verstache.gabella.dto.ServerDto;
import ru.verstache.gabella.model.Server;
import ru.verstache.gabella.model.stats.ServerStats;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ServerServerTest extends PostgresTestContainerInitializer {

    @Autowired
    private ServerService uut;

    @Test
    void shouldFindMostPopularServer() {
        List<ServerDto> mostPopularServers = uut.findMostPopularServers(2);

        assertThat(mostPopularServers).hasSize(2);
        assertThat(mostPopularServers.get(0).name()).isEqualTo("rush_b");
        assertThat(mostPopularServers.get(1).name()).isEqualTo("rush_a");
    }

    @Test
    void shouldGenerateNewServer_whenNameNotExistsYet() {
        final String nonExistingName = "non_existing_name";
        Server server = uut.findByName(nonExistingName);
        assertNotNull(server.getId());
        assertThat(server.getName()).isEqualTo(nonExistingName);
    }

    @Test
    void shouldPrepareServerStats() {
        final UUID serverId = UUID.fromString("db7b1d54-ff13-48d0-b6c7-06ba5507a305");
        ServerStats serversStats = uut.getServersStats(serverId);
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
