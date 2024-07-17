package ru.verstache.gabella.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.verstache.gabella.PostgresTestContainerInitializer;
import ru.verstache.gabella.dto.MatchDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(initializers = PostgresTestContainerInitializer.class)
public class MatchServiceIntTest {

    @Autowired
    private MatchService sut;

    @Test
    void shouldFindLastMatches() {
        List<MatchDto> lastMatches = sut.findLastMatches(2);
        assertThat(lastMatches.get(0).finishedAt()).isEqualTo(LocalDateTime.of(2024, 6, 27, 21, 39, 21, 311000000));
        assertThat(lastMatches.get(1).finishedAt()).isEqualTo(LocalDateTime.of(2024, 6, 24, 22, 12, 41, 435000000));
    }

    @Test
    void shouldFindMatchesByServerIdAndFinishedDate() {
        List<MatchDto> matchesByServerAndFinishedDate = sut.findByServerIdAndDate(UUID.fromString("db7b1d54-ff13-48d0-b6c7-06ba5507a305"), LocalDate.of(2024, 6, 24));
        assertThat(matchesByServerAndFinishedDate).hasSize(2);
    }
}
