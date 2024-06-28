package ru.verstache.gabella.service;

import ru.verstache.gabella.dto.ServerDto;
import ru.verstache.gabella.model.Server;
import ru.verstache.gabella.model.stats.ServerStats;

import java.util.Set;
import java.util.UUID;

public interface ServerService {
    Server findByName(String serverName);

    Set<ServerDto> findAll();

    ServerStats getServersStats(UUID serverId);

    ServerDto findById(UUID serverId);
}
