package ru.verstache.gabella.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.verstache.gabella.dto.MatchDto;
import ru.verstache.gabella.mapper.MatchMapper;
import ru.verstache.gabella.model.Match;
import ru.verstache.gabella.repository.MatchRepository;
import ru.verstache.gabella.repository.MatchWinnerRepository;
import ru.verstache.gabella.service.MatchService;
import ru.verstache.gabella.service.PlayerService;
import ru.verstache.gabella.service.ServerService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
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
    private final ServerService serverService;

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
    public List<MatchDto> findLastMatches(Integer amount) {
        PageRequest pageRequest = PageRequest.of(0, amount);
        return matchRepository.findLastFinished(pageRequest).stream()
                .map(matchMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MatchDto> findByServerIdAndDate(UUID serverId, LocalDate date) {
        return matchRepository.findAllByServerAndFinishedAtBetween(
                        serverService.findById(serverId),
                        LocalDateTime.of(date, LocalTime.MIDNIGHT),
                        LocalDateTime.of(date.plusDays(1L), LocalTime.MIDNIGHT))
                .stream()
                .map(matchMapper::toDto)
                .collect(Collectors.toList());
    }


    private boolean isDuplicated(Match match) {
        return !matchRepository.findAllByServerAndStartedAtAndFinishedAt(match.getServer(), match.getStartedAt(), match.getFinishedAt()).isEmpty();
    }
}
