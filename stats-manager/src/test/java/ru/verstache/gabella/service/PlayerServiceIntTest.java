package ru.verstache.gabella.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.verstache.gabella.PostgresTestContainerInitializer;
import ru.verstache.gabella.model.stats.PlayerStats;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class PlayerServiceIntTest  extends PostgresTestContainerInitializer {

    @Autowired
    private PlayerService uut;

    @Test
    void shouldPreparePlayerStats() {
        PlayerStats playerStats = uut.getStats("Morty");
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
}
