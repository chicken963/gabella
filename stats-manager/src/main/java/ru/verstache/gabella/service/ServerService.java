package ru.verstache.gabella.service;

import ru.verstache.gabella.dto.ServerDto;
import ru.verstache.gabella.model.Server;
import ru.verstache.gabella.model.stats.ServerStats;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ServerService {
    Server findByName(String serverName);

    Set<ServerDto> findAll();

    ServerStats getServerStats(UUID serverId);

    ServerDto findById(UUID serverId);

    List<ServerDto> findMostPopularServers(Integer amount);
}
