package ru.verstache.gabella.service;

import ru.verstache.gabella.model.Match;
import ru.verstache.gabella.model.Player;
import ru.verstache.gabella.model.stats.PlayerStats;

import java.util.Map;
import java.util.Set;

public interface PlayerService {

    Player findPlayerByNick(String nick);

    Set<Player> increaseStatsForParticipants(Match match);

    void increaseStatsForWinners(Match match);

    Set<Player> extractWinners(Match match, Set<Player> participants);

    int getLongestWinStreak(Player player);

    PlayerStats getStats(String nick);
}
