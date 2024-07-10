package ru.verstache.gabella.service;

import ru.verstache.gabella.model.Match;
import ru.verstache.gabella.model.Player;

import java.util.Set;

public interface MatchWinnerService {

    Set<Player> getWinners(Match match);
}
