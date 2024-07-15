package ru.verstache.gabella.service;

import ru.verstache.gabella.dto.PlayerDto;
import ru.verstache.gabella.model.Match;
import ru.verstache.gabella.model.Player;
import ru.verstache.gabella.model.stats.PlayerStats;

import java.util.List;
import java.util.Set;

public interface PlayerService {

    Player findPlayerByNick(String nick);

    Set<Player> increaseStatsForParticipants(Match match);

    void increaseStatsForWinners(Match match);

    int getLongestWinStreak(Player player);

    PlayerStats getStats(String nick);

    List<PlayerDto> findTopPlayers(Integer amount);
}
