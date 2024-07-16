package ru.verstache.gabella.mapper;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.verstache.gabella.PostgresTestContainerInitializer;
import ru.verstache.gabella.dto.MatchDto;
import ru.verstache.gabella.dto.PlayerDto;
import ru.verstache.gabella.dto.ServerDto;
import ru.verstache.gabella.dto.WinnerDto;
import ru.verstache.gabella.model.Match;
import ru.verstache.gabella.model.Player;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(initializers = PostgresTestContainerInitializer.class)
public class MatchMapperTest {

    @Autowired
    private MatchMapper sut;

    @Test
    void shouldMapToModel() {
        MatchDto matchDto = new MatchDto(
            new ServerDto(UUID.fromString("78de6864-36e9-4563-88e5-a7fef2e07cec"),
                "rush_a",
                LocalDateTime.of(2024, 6, 28, 19, 45, 33, 514671000)),
            LocalDateTime.of(2024, 7, 16, 16, 10, 18, 592852000),
            LocalDateTime.of(2024, 7, 16, 18, 2, 51, 52691000),
            Set.of(
                new PlayerDto("Nick"),
                new PlayerDto("Morty"),
                new PlayerDto("David")
            ),
            Set.of(
                new WinnerDto("Nick", 5),
                new WinnerDto("Morty", 14)
            )
        );
        Match match = sut.toModel(matchDto);

        assertThat(match.getMatchWinners()).hasSize(2);
        assertThat(match.getMatchWinners()).haveExactly(1, new Condition<>(matchWinner -> matchWinner.getWinner().getId().equals(UUID.fromString("63bc807c-96ad-4d65-aae5-a657663afa14")), "Morty found in db"));
        assertThat(match.getMatchWinners()).haveExactly(1, new Condition<>(matchWinner -> matchWinner.getWinner().getId().equals(UUID.fromString("d8eead27-ac5c-45e4-a9d4-12d1ef323cd6")), "Nick found in db"));

        assertThat(match.getParticipants()).hasSize(3);
        assertThat(match.getParticipants()).haveExactly(1, new Condition<>(player -> player.getNick().equals("Nick"), "Nick is found in db"));
        assertThat(match.getParticipants()).haveExactly(1, new Condition<>(player -> player.getNick().equals("Morty"), "Morty is found in db"));
        assertThat(match.getParticipants()).haveExactly(1, new Condition<>(player -> player.getNick().equals("David"), "David is created in db"));
        assertThat(match.getParticipants().stream()
                .filter(player -> player.getNick().equals("David"))
                .map(Player::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()))
            .hasSize(1);
    }
}
