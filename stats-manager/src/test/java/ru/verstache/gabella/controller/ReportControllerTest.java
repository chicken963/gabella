package ru.verstache.gabella.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.verstache.gabella.PostgresTestContainerInitializer;
import ru.verstache.gabella.dto.MatchDto;
import ru.verstache.gabella.dto.PlayerDto;
import ru.verstache.gabella.dto.ServerDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(initializers = PostgresTestContainerInitializer.class)
public class ReportControllerTest {

    @Autowired
    private ReportController sut;

    @Test
    void shouldGetLastMatches() {
        ResponseEntity<List<MatchDto>> lastMatches = sut.getLastMatches(2);
        assertThat(lastMatches.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<MatchDto> matchesList = lastMatches.getBody();
        assertThat(matchesList).hasSize(2);
        assertThat(matchesList.get(0).finishedAt()).isEqualTo(LocalDateTime.of(2024, 6, 27, 21, 39, 21, 311000000));
        assertThat(matchesList.get(1).finishedAt()).isEqualTo(LocalDateTime.of(2024, 6, 24, 22, 12, 41, 435000000));
    }

    @Test
    void shouldGetTopPlayers() {
        ResponseEntity<List<PlayerDto>> topPlayers = sut.getTopPlayers(3);
        assertThat(topPlayers.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<PlayerDto> playersList = topPlayers.getBody();
        assertThat(playersList).hasSize(3);
        assertThat(playersList.get(0).nick()).isEqualTo("Morty");
        assertThat(playersList.get(1).nick()).isEqualTo("Nick");
        assertThat(playersList.get(2).nick()).isEqualTo("Innha");
    }

    @Test
    void shouldGetMostPopularServers() {
        ResponseEntity<List<ServerDto>> mostPopularServers = sut.getMostPopularServers(2);
        assertThat(mostPopularServers.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<ServerDto> serversList = mostPopularServers.getBody();
        assertThat(serversList.get(0).name()).isEqualTo("rush_b");
        assertThat(serversList.get(1).name()).isEqualTo("rush_a");
    }
}
