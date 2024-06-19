package ru.verstache.gabella.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.verstache.gabella.model.Match;
import ru.verstache.gabella.model.Player;
import ru.verstache.gabella.model.stats.PlayerStats;
import ru.verstache.gabella.repository.PlayerRepository;
import ru.verstache.gabella.service.PlayerService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

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
    public Set<Player> increaseStatsForWinners(Set<Player> winners) {
        winners.forEach(winner -> {
                    winner.setWonMatches(winner.getWonMatches() + 1);
                    winner.setPoints(winner.getPoints() + (int) Math.floor(Math.random() * 15));
                }
                );
        log.info("Updating win statistics for players {}", getPlayersNicknames(winners));
        return new HashSet<>(playerRepository.saveAll(winners));
    }

    @Override
    public Set<Player> extractWinners(Match match, Set<Player> participants) {
        Set<Player> winners = participants.stream()
                .filter(participant -> match.getWinners().stream()
                        .anyMatch(winner -> winner.getNick().equals(participant.getNick())))
                .collect(Collectors.toSet());
        match.setWinners(winners);
        return winners;
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
            if (match.getWinners().stream().anyMatch(winner -> winner.getNick().equals(player.getNick()))) {
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

    private Player generateNewPlayer(String nick) {
        return Player.builder()
                .id(UUID.randomUUID())
                .nick(nick)
                .registeredAt(LocalDateTime.now())
                .totalMatches(0)
                .wonMatches(0)
                .points(0)
                .build();
    }

    private Set<Player> getTeammates(Match match, Player player) {
        return match.getParticipants().stream()
                .filter(participant -> match.getWinners().contains(player) == match.getWinners().contains(participant))
                .collect(Collectors.toSet());
    }

    private Set<Player> getEnemies(Match match, Player player) {
        return match.getParticipants().stream()
                .filter(participant -> match.getWinners().contains(player) != match.getWinners().contains(participant))
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
