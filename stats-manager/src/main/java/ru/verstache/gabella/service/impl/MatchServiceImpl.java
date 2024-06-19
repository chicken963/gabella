package ru.verstache.gabella.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.verstache.gabella.dto.MatchDto;
import ru.verstache.gabella.mapper.MatchMapper;
import ru.verstache.gabella.model.Match;
import ru.verstache.gabella.model.Player;
import ru.verstache.gabella.repository.MatchRepository;
import ru.verstache.gabella.service.MatchService;
import ru.verstache.gabella.service.PlayerService;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
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
        Set<Player> participants = playerService.increaseStatsForParticipants(match);
        Set<Player> winners = playerService.extractWinners(match, participants);
        playerService.increaseStatsForWinners(winners);
        matchRepository.save(match);
        log.info("Saved new match to statistics");
    }


    private boolean isDuplicated(Match match) {
        return !matchRepository.findAllByServerAndStartedAtAndFinishedAt(match.getServer(), match.getStartedAt(), match.getFinishedAt()).isEmpty();
    }
}
