package ru.verstache.gabella.mapper;

import org.mapstruct.Mapper;
import ru.verstache.gabella.dto.WinnerDto;
import ru.verstache.gabella.model.MatchWinner;

@Mapper(componentModel = "spring")
public abstract class WinnerMapper {

    public WinnerDto toDto(MatchWinner matchWinner) {
        return new WinnerDto(matchWinner.getWinner().getNick(), matchWinner.getPoints());
    }
}
