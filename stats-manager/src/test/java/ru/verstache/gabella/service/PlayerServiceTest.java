package ru.verstache.gabella.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.verstache.gabella.IntArrayConverter;
import ru.verstache.gabella.mapper.PlayerMapper;
import ru.verstache.gabella.model.Match;
import ru.verstache.gabella.model.MatchWinner;
import ru.verstache.gabella.model.Player;
import ru.verstache.gabella.repository.PlayerRepository;
import ru.verstache.gabella.service.impl.PlayerServiceImpl;
import utils.PlayerUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.PlayerUtils.generateNewPlayer;

@Slf4j
@ExtendWith(SpringExtension.class)
public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private MatchWinnerService matchWinnerService;

    @Mock
    private PlayerMapper playerMapper;


    @InjectMocks
    private PlayerServiceImpl uut;

    @Test
    public void shouldGenerateNewPlayer_whenNotFoundInDb() {
        final String nick = "Nick";
        Player player = uut.findPlayerByNick(nick);
        assertEquals(nick, player.getNick());
        assertEquals(0, player.getTotalMatches());
        assertEquals(0, player.getWonMatches());
        assertEquals(0, player.getPoints());
    }

    @Test
    public void shouldIncreaseStatsForParticipants() {
        Match match = mock(Match.class);
        Set<Player> participants = Stream.of(3, 17, 11)
                .map(totalMatches -> Player.builder().totalMatches(totalMatches).build())
                .collect(Collectors.toSet());
        when(match.getParticipants()).thenReturn(participants);
        when(playerRepository.saveAll(participants)).thenReturn(new ArrayList<>(participants));

        Set<Player> players = uut.increaseStatsForParticipants(match);
        Stream.of(4, 18, 12).forEach(totalMatches ->
                assertTrue(players.stream().anyMatch(player -> Objects.equals(player.getTotalMatches(), totalMatches))));
    }

    @Test
    public void shouldIncreaseStatsForWinners() {
        Match match = mock(Match.class);

        Set<MatchWinner> winners = Stream.of("Andrew", "Eddie")
                .map(PlayerUtils::generateNewPlayer)
                .map(player -> {
                    MatchWinner matchWinner = new MatchWinner();
                    matchWinner.setWinner(player);
                    return matchWinner;
                })
                .collect(Collectors.toSet());

        Set<Player> players = Stream.of("Andrew", "Betty", "Eddie")
                .map(PlayerUtils::generateNewPlayer)
                .collect(Collectors.toSet());

        when(match.getMatchWinners()).thenReturn(winners);

        when(match.getParticipants()).thenReturn(players);

        uut.increaseStatsForWinners(match);

        players.stream().filter(player -> !"Betty".equals(player.getNick()))
                .peek(player -> log.info("Analyzing number of won matches for player {}", player.getNick()))
                .forEach(player -> assertEquals(1, player.getWonMatches()));

        players.stream().filter(player -> "Betty".equals(player.getNick()))
                .peek(player -> log.info("Analyzing number of won matches for player {}", player.getNick()))
                .forEach(player -> assertEquals(0, player.getWonMatches()));

    }

    @ParameterizedTest
    @CsvSource(value = {
            "0;1",
            "0,4;1",
            "0,2,4,6,8;1",
            "0,2,4,6,8,9;2",
            "0,2,4,6,7,8;3",
            "0,1,4,6,7,8;3",
            "0,1,2,6,7,8;3",
            "1,5,6,7,8;4",
            "1,2,3,4,5,9;5",
            "0,1,2,3,4,5,6,7,8,9;10"}, delimiterString = ";")
    public void shouldGetLongestWinStreak(@ConvertWith(IntArrayConverter.class) int[] wonMatchesIndexes, int expectedLongestStreak) {
        Player targetPlayer = generateNewPlayer("Nick");
        Set<Player> otherPlayers = Stream.of("Anna", "Andrew", "Paul", "Tonny")
                .map(PlayerUtils::generateNewPlayer)
                .collect(Collectors.toSet());
        Set<Match> matchesFromDb = IntStream.range(0, 10).mapToObj((index) -> {
            Match match = new Match();
            match.setParticipants(new HashSet<>(otherPlayers));
            match.setFinishedAt(LocalDateTime.of(2024, 7, 15, 4, 20, index));
            return match;
        })
        .collect(Collectors.toSet());
        targetPlayer.setMatches(matchesFromDb);

        Arrays.stream(wonMatchesIndexes).forEach(wonMatchesIndex ->
                matchesFromDb.stream()
                        .filter(match -> match.getFinishedAt().getSecond() == wonMatchesIndex)
                        .findFirst()
                        .ifPresent(match -> match.getParticipants().add(targetPlayer)));

        matchesFromDb.forEach(match ->
                when(matchWinnerService.getWinners(match)).thenReturn(match.getParticipants())
        );

        int longestWinStreak = uut.getLongestWinStreak(targetPlayer);

        assertEquals(expectedLongestStreak, longestWinStreak);

    }

}
