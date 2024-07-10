package ru.verstache.gabella.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.verstache.gabella.dto.MatchDto;
import ru.verstache.gabella.mapper.MatchMapper;
import ru.verstache.gabella.model.Match;
import ru.verstache.gabella.model.MatchWinner;
import ru.verstache.gabella.model.MatchWinnerId;
import ru.verstache.gabella.model.Player;
import ru.verstache.gabella.repository.MatchRepository;
import ru.verstache.gabella.repository.MatchWinnerRepository;
import ru.verstache.gabella.service.MatchService;
import ru.verstache.gabella.service.PlayerService;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final MatchWinnerRepository matchWinnerRepository;
    private final MatchMapper matchMapper;
    private final PlayerService playerService;

    @Override
    public void saveResult(MatchDto matchDto) {
        Match match = matchMapper.toModel(matchDto);
        if (isDuplicated(match)) {
            log.warn("Duplicated data - match on server {} started at {} was already saved in statistics",
                    match.getServer().getName(),
                    match.getStartedAt());
            return;
        }
        updatePlayerStats(match);
        matchRepository.save(match);
        match.getMatchWinners()
                .stream().peek(matchWinner -> matchWinner.setId(matchMapper.generateMatchWinnerId(matchWinner)))
                .forEach(matchWinnerRepository::save);

        log.info("Saved new match to statistics");
    }

    @Transactional
    void updatePlayerStats(Match match) {
        playerService.increaseStatsForParticipants(match);
        playerService.increaseStatsForWinners(match);
    }

    @Override
    public Set<Player> getWinners(Match match) {
        return match.getMatchWinners().stream()
                .map(MatchWinner::getWinner)
                .collect(Collectors.toSet());
    }


    private boolean isDuplicated(Match match) {
        return !matchRepository.findAllByServerAndStartedAtAndFinishedAt(match.getServer(), match.getStartedAt(), match.getFinishedAt()).isEmpty();
    }
}
