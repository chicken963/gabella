package ru.verstache.gabella.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.verstache.gabella.service.MatchService;
import ru.verstache.gabella.service.ServerService;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class ServerControllerTest {

    @Mock
    private ServerService serverService;

    @Mock
    private MatchService matchService;

    @InjectMocks
    private ServerController uut;

    @Test
    void shouldGetServersInfo() {
        uut.getServersInfo();
        verify(serverService, times(1)).findAll();
        verify(serverService, times(0)).getServerStats(any());
        verify(serverService, times(0)).findDtoById(any());
    }

    @Test
    void shouldGetServerStats() {
        UUID serverId = UUID.randomUUID();
        uut.getServerStats(serverId);
        verify(serverService, times(0)).findAll();
        verify(serverService, times(1)).getServerStats(serverId);
        verify(serverService, times(0)).findDtoById(any());
    }

    @Test
    void shouldGetServerInfo() {
        UUID serverId = UUID.randomUUID();
        uut.getServerInfo(serverId);
        verify(serverService, times(0)).findAll();
        verify(serverService, times(0)).getServerStats(serverId);
        verify(serverService, times(1)).findDtoById(any());
    }

    @Test
    void shouldFindMatchesByServerIdAndFinishedDate() {
        UUID serverId = UUID.randomUUID();
        LocalDate targetDate = LocalDate.now();
        uut.getServerMatchesByDate(serverId, targetDate);
        verify(matchService, times(1)).findByServerIdAndDate(serverId, targetDate);
    }
}
