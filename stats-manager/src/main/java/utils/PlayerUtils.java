package utils;

import ru.verstache.gabella.model.Player;

import java.time.LocalDateTime;
import java.util.UUID;

public class PlayerUtils {

    public static Player generateNewPlayer(String nick) {
        return Player.builder()
                .id(UUID.randomUUID())
                .nick(nick)
                .registeredAt(LocalDateTime.now())
                .totalMatches(0)
                .wonMatches(0)
                .points(0)
                .build();
    }
}
