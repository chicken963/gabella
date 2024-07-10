package ru.verstache.gabella.service;

import ru.verstache.gabella.dto.MatchDto;
import ru.verstache.gabella.model.Match;
import ru.verstache.gabella.model.Player;

import java.util.List;
import java.util.Set;

public interface MatchService {

    void saveResult(MatchDto matchDto);

    Set<Player> getWinners(Match match);

    List<MatchDto> findLastMatches(Integer amount);
}
