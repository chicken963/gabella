package ru.verstache.gabella.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.verstache.gabella.dto.PlayerDto;
import ru.verstache.gabella.model.Player;
import ru.verstache.gabella.service.PlayerService;

@Mapper(componentModel = "spring")
public abstract class PlayerMapper {

    @Autowired
    private PlayerService playerService;

    public PlayerDto toDto(Player player) {
        return new PlayerDto(player.getNick());
    }

    Player toModel(PlayerDto playerDto) {
        return playerService.findPlayerByNick(playerDto.nick());
    }

}
