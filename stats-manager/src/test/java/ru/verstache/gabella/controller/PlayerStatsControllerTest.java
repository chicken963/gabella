package ru.verstache.gabella.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.verstache.gabella.service.PlayerService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class PlayerStatsControllerTest {

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private PlayerStatsController uut;

    @Test
    void shouldGetPlayerStats() {
        final String nick = "anyNick";
        uut.getPlayerStats(nick);
        verify(playerService, times(1)).getStats(nick);
    }
}
