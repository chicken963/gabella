package ru.verstache.gabella.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ru.verstache.gabella.dto.MatchDto;
import ru.verstache.gabella.model.Match;
import ru.verstache.gabella.model.MatchWinner;
import ru.verstache.gabella.model.MatchWinnerId;
import ru.verstache.gabella.model.Player;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class MatchMapper {

    @Autowired
    private ServerMapper serverMapper;

    @Autowired
    private PlayerMapper playerMapper;


    public Match toModel(MatchDto matchDto) {
        Match match = Match.builder()
                .id(UUID.randomUUID())
                .startedAt(matchDto.startedAt())
                .finishedAt(matchDto.finishedAt())
                .server(serverMapper.toModel(matchDto.server()))
                .participants(matchDto.participants().stream()
                        .map(playerMapper::toModel)
                        .collect(Collectors.toSet()))
                .build();
        match.setMatchWinners(matchDto.winners().stream().map(winner -> {
            MatchWinner matchWinner = new MatchWinner();
            matchWinner.setMatch(match);
            matchWinner.setPoints(winner.points());
            matchWinner.setWinner(match.getParticipants().stream()
                    .filter(pl -> pl.getNick().equals(winner.nick()))
                    .findFirst()
                    .orElse(null));
//            matchWinner.setId(generateMatchWinnerId(matchWinner));
            return matchWinner;
        }).collect(Collectors.toSet()));
        return match;
    }

    public MatchWinnerId generateMatchWinnerId(MatchWinner matchWinner) {
        MatchWinnerId matchWinnerId = new MatchWinnerId();
        matchWinnerId.setMatchId(matchWinner.getMatch().getId());
        matchWinnerId.setWinnerId(matchWinner.getWinner().getId());
        return matchWinnerId;
    }

}
