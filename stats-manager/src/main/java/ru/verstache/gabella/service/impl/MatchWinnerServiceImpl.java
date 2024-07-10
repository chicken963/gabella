package ru.verstache.gabella.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.verstache.gabella.model.Match;
import ru.verstache.gabella.model.MatchWinner;
import ru.verstache.gabella.model.Player;
import ru.verstache.gabella.service.MatchWinnerService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchWinnerServiceImpl implements MatchWinnerService {

    @Override
    public Set<Player> getWinners(Match match) {
        return match.getMatchWinners().stream()
                .map(MatchWinner::getWinner)
                .collect(Collectors.toSet());
    }
}
