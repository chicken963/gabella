package ru.verstache.gabella.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.verstache.gabella.dto.MatchDto;
import ru.verstache.gabella.model.Match;

@Mapper(componentModel = "spring", uses = {
        ServerMapper.class,
        PlayerMapper.class
})
public interface MatchMapper {

    @Mapping(target = "id", ignore = true)
    Match toModel(MatchDto matchDto);

    MatchDto toDto(Match match);

}
