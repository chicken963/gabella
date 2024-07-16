package ru.verstache.gabella.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.verstache.gabella.PostgresTestContainerInitializer;
import ru.verstache.gabella.dto.PlayerDto;
import ru.verstache.gabella.model.stats.PlayerStats;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(initializers = PostgresTestContainerInitializer.class)
public class PlayerServiceIntTest {

    @Autowired
    private PlayerService sut;

    @Test
    void shouldPreparePlayerStats() {
        PlayerStats playerStats = sut.getStats("Morty");
        PlayerStats expectedPlayerStats = PlayerStats.builder()
                .mostOftenEnemy("Nick")
                .points(20)
                .wonMatches(2)
                .totalMatches(3)
                .longestWinStreak(2)
                .registeredAt(LocalDateTime.of(2024, 7, 8, 11, 9, 23, 707918000))
                .nick("Morty")
                .build();
        assertEquals(expectedPlayerStats, playerStats);
    }

    @Test
    void shouldFindTopPlayers() {
        List<PlayerDto> topPlayers = sut.findTopPlayers(2);
        assertThat(topPlayers.get(0).nick()).isEqualTo("Morty");
        assertThat(topPlayers.get(1).nick()).isEqualTo("Nick");
    }
}
