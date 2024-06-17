package ru.verstache.gabella.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.verstache.gabella.model.Server;
import ru.verstache.gabella.repository.ServerRepository;
import ru.verstache.gabella.service.ServerService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServerServiceImpl implements ServerService {

    private final ServerRepository serverRepository;

    @Override
    public Server findByName(String serverName) {
        return serverRepository.findByName(serverName)
                .orElseGet(() -> serverRepository.save(generateNewServer(serverName)));
    }

    private Server generateNewServer(String serverName) {
        return Server.builder()
                .id(UUID.randomUUID())
                .name(serverName)
                .build();
    }
}
