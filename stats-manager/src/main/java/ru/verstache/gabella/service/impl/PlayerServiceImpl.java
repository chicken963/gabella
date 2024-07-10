package ru.verstache.gabella.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.verstache.gabella.dto.PlayerDto;
import ru.verstache.gabella.mapper.PlayerMapper;
import ru.verstache.gabella.model.Match;
import ru.verstache.gabella.model.MatchWinner;
import ru.verstache.gabella.model.Player;
import ru.verstache.gabella.model.stats.PlayerStats;
import ru.verstache.gabella.repository.PlayerRepository;
import ru.verstache.gabella.service.MatchWinnerService;
import ru.verstache.gabella.service.PlayerService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static utils.PlayerUtils.generateNewPlayer;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final MatchWinnerService matchWinnerService;
    private final PlayerMapper playerMapper;

    @Override
    public Player findPlayerByNick(String nick) {
        return playerRepository.findByNick(nick)
                .orElseGet(() -> {
                    log.info("Player {} was not tracked before - if match data is provided correctly, player will be saved for further tracking", nick);
                    return generateNewPlayer(nick);
                });
    }

    @Override
    public Set<Player> increaseStatsForParticipants(Match match) {
        Set<Player> participants = match.getParticipants();
        participants.forEach(participant ->
                participant.setTotalMatches(participant.getTotalMatches() + 1));
        log.info("Updating play statistics for players {}", getPlayersNicknames(participants));
        return new HashSet<>(playerRepository.saveAll(participants));
    }

    @Override
    public void increaseStatsForWinners(Match match) {
        Set<Player> winnersFromDb = match.getParticipants().stream().filter(
                participant -> match.getMatchWinners().stream()
                        .map(MatchWinner::getWinner)
                        .map(Player::getNick)
                        .collect(Collectors.toSet())
                .contains(participant.getNick())
        ).collect(Collectors.toSet());

        winnersFromDb.forEach(winner -> {
                    winner.setWonMatches(winner.getWonMatches() + 1);
                    winner.setPoints(winner.getPoints() + match.getMatchWinners().stream()
                            .filter(matchWinner -> matchWinner.getWinner().equals(winner))
                            .findFirst()
                            .map(MatchWinner::getPoints)
                            .orElse(0));
                }
                );
        log.info("Updating win statistics for players {}", getPlayersNicknames(winnersFromDb));
        playerRepository.saveAll(winnersFromDb);
    }

    @Override
    public Set<Player> extractWinners(Match match, Set<Player> participants) {
        return participants.stream()
                .filter(participant -> matchWinnerService.getWinners(match).stream()
                        .anyMatch(winner -> winner.getNick().equals(participant.getNick())))
                .collect(Collectors.toSet());
    }

    @Override
    public int getLongestWinStreak(Player player) {
        List<Match> matches = new ArrayList<>(
                Optional.ofNullable(player.getMatches())
                        .orElse(Collections.emptySet())
        );
        matches.sort(Comparator.comparing(Match::getFinishedAt));
        int longestStreak = 0;
        int currentStreak = 0;
        for (Match match : matches) {
            if (matchWinnerService.getWinners(match).stream().anyMatch(winner -> winner.getNick().equals(player.getNick()))) {
                currentStreak++;
            } else {
                if (currentStreak > longestStreak) {
                    longestStreak = currentStreak;
                }
                currentStreak = 0;
            }
        }
        return Math.max(longestStreak, currentStreak);
    }

    @Override
    public PlayerStats getStats(String nick) {
        Player player = findPlayerByNick(nick);
        return PlayerStats.builder()
                .nick(nick)
                .registeredAt(player.getRegisteredAt())
                .longestWinStreak(getLongestWinStreak(player))
                .totalMatches(player.getTotalMatches())
                .wonMatches(player.getWonMatches())
                .points(player.getPoints())
                .mostOftenTeammate(getMostOftenTeammate(player))
                .mostOftenEnemy(getMostOftenEnemies(player))
                .build();
    }

    @Override
    public List<PlayerDto> findTopPlayers(Integer amount) {
        PageRequest pageRequest = PageRequest.of(0, amount);
        return playerRepository.findTopPlayers(pageRequest).stream()
                .map(playerMapper::toDto)
                .collect(Collectors.toList());
    }

    private String getMostOftenTeammate(Player player) {
        return getMostOftenPlayers(player, this::getTeammates);
    }

    private String getMostOftenEnemies(Player player) {
        return getMostOftenPlayers(player, this::getEnemies);
    }

    private String getMostOftenPlayers(Player player, PlayersExtractor extractor) {
        return player.getMatches()
                .stream()
                .map(match -> extractor.extractPlayers(match, player))
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(Map.Entry::getKey)
                .map(Player::getNick)
                .findFirst()
                .orElse(null);
    }

    private Set<Player> getTeammates(Match match, Player player) {
        Set<Player> winners = matchWinnerService.getWinners(match);
        return match.getParticipants().stream()
                .filter(participant -> winners.contains(player) == winners.contains(participant))
                .filter(participant -> participant != player)
                .collect(Collectors.toSet());
    }

    private Set<Player> getEnemies(Match match, Player player) {
        Set<Player> winners = matchWinnerService.getWinners(match);
        return match.getParticipants().stream()
                .filter(participant -> winners.contains(player) != winners.contains(participant))
                .collect(Collectors.toSet());
    }

    private String getPlayersNicknames(Set<Player> winners) {
        return winners.stream()
                .map(Player::getNick)
                .collect(Collectors.joining(", "));
    }

    @FunctionalInterface
    private interface PlayersExtractor {
        Set<Player> extractPlayers(Match match, Player player);
    }
}
