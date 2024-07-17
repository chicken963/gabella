package ru.verstache.gabella.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.verstache.gabella.dto.MatchDto;
import ru.verstache.gabella.dto.PlayerDto;
import ru.verstache.gabella.dto.WinnerDto;
import ru.verstache.gabella.model.Match;
import ru.verstache.gabella.model.MatchWinner;
import ru.verstache.gabella.model.MatchWinnerId;

import java.util.UUID;
import java.util.stream.Collectors;

import static utils.FilterUtils.distinctByKey;

@Mapper(componentModel = "spring")
public abstract class MatchMapper {

    @Autowired
    private ServerMapper serverMapper;

    @Autowired
    private PlayerMapper playerMapper;

    @Autowired
    private WinnerMapper winnerMapper;


    public Match toModel(MatchDto matchDto) {
        Match match = Match.builder()
            .id(UUID.randomUUID())
            .startedAt(matchDto.startedAt())
            .finishedAt(matchDto.finishedAt())
            .server(serverMapper.toModel(matchDto.server()))
            .participants(matchDto.participants().stream()
                .filter(distinctByKey(PlayerDto::nick))
                .map(playerMapper::toModel)
                .collect(Collectors.toSet()))
            .build();
        match.setMatchWinners(matchDto.winners().stream()
            .filter(distinctByKey(WinnerDto::nick))
            .map(winner -> {
                MatchWinner matchWinner = new MatchWinner();
                matchWinner.setMatch(match);
                matchWinner.setPoints(winner.points());
                matchWinner.setWinner(match.getParticipants().stream()
                    .filter(pl -> pl.getNick().equals(winner.nick()))
                    .findFirst()
                    .orElse(null));
            return matchWinner;
        }).collect(Collectors.toSet()));
        return match;
    }

    public MatchDto toDto(Match match) {
        return new MatchDto(
                serverMapper.toDto(match.getServer()),
                match.getStartedAt(),
                match.getFinishedAt(),
                match.getParticipants().stream().map(playerMapper::toDto).collect(Collectors.toSet()),
                match.getMatchWinners().stream().map(winnerMapper::toDto).collect(Collectors.toSet())
        );
    }

    public MatchWinnerId generateMatchWinnerId(MatchWinner matchWinner) {
        MatchWinnerId matchWinnerId = new MatchWinnerId();
        matchWinnerId.setMatchId(matchWinner.getMatch().getId());
        matchWinnerId.setWinnerId(matchWinner.getWinner().getId());
        return matchWinnerId;
    }

}
