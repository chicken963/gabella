package ru.verstache.gabella.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.verstache.gabella.service.ServerService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class ServerControllerTest {

    @Mock
    private ServerService serverService;

    @InjectMocks
    private ServerController uut;

    @Test
    void shouldGetServersInfo() {
        uut.getServersInfo();
        verify(serverService, times(1)).findAll();
        verify(serverService, times(0)).getServerStats(any());
        verify(serverService, times(0)).findById(any());
    }

    @Test
    void shouldGetServerStats() {
        UUID serverId = UUID.randomUUID();
        uut.getServerStats(serverId);
        verify(serverService, times(0)).findAll();
        verify(serverService, times(1)).getServerStats(serverId);
        verify(serverService, times(0)).findById(any());
    }

    @Test
    void shouldGetServerInfo() {
        UUID serverId = UUID.randomUUID();
        uut.getServerInfo(serverId);
        verify(serverService, times(0)).findAll();
        verify(serverService, times(0)).getServerStats(serverId);
        verify(serverService, times(1)).findById(any());
    }
}
