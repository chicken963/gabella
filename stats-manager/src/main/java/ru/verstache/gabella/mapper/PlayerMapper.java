package ru.verstache.gabella.mapper;

import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.verstache.gabella.dto.PlayerDto;
import ru.verstache.gabella.model.Player;
import ru.verstache.gabella.repository.PlayerRepository;

import static utils.PlayerUtils.generateNewPlayer;

@Slf4j
@Mapper(componentModel = "spring")
public abstract class PlayerMapper {

    @Autowired
    private PlayerRepository playerRepository;

    public PlayerDto toDto(Player player) {
        return new PlayerDto(player.getNick());
    }

    Player toModel(PlayerDto playerDto) {
        String nick = playerDto.nick();
        return playerRepository.findByNick(nick)
                .orElseGet(() -> {
                    log.info("Player {} was not tracked before - if match data is provided correctly, player will be saved for further tracking", nick);
                    return generateNewPlayer(nick);
                });
    }

}
