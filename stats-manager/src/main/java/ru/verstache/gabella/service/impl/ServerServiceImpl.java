package ru.verstache.gabella.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.verstache.gabella.dto.PlayerDto;
import ru.verstache.gabella.dto.ServerDto;
import ru.verstache.gabella.exception.ItemNotFoundException;
import ru.verstache.gabella.mapper.PlayerMapper;
import ru.verstache.gabella.mapper.ServerMapper;
import ru.verstache.gabella.model.Match;
import ru.verstache.gabella.model.Player;
import ru.verstache.gabella.model.Server;
import ru.verstache.gabella.model.stats.ServerStats;
import ru.verstache.gabella.repository.MatchRepository;
import ru.verstache.gabella.repository.ServerRepository;
import ru.verstache.gabella.service.ServerService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServerServiceImpl implements ServerService {

    private final ServerRepository serverRepository;
    private final ServerMapper serverMapper;
    private final MatchRepository matchRepository;
    private final PlayerMapper playerMapper;

    @Override
    public Server findByName(String serverName) {
        return serverRepository.findByName(serverName)
                .orElseGet(() -> serverRepository.save(generateNewServer(serverName)));
    }

    @Override
    public Set<ServerDto> findAll() {
        return serverRepository.findAll()
                .stream()
                .map(serverMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public ServerStats getServersStats(UUID serverId) {
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new ItemNotFoundException("No server found by id " + serverId));
        return ServerStats.builder()
                .name(server.getName())
                .createdAt(server.getCreatedAt())
                .totalPlayedMatches(matchRepository.countMatchByServer(server))
                .bestPlayer(findBestPlayer(server))
                .build();
    }

    private PlayerDto findBestPlayer(Server server) {
        return matchRepository.findAllByServer(server)
                .stream()
                .map(Match::getWinners)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(Map.Entry::getKey)
                .map(playerMapper::toDto)
                .findFirst()
                .orElse(null);
    }

    private Server generateNewServer(String serverName) {
        return Server.builder()
                .id(UUID.randomUUID())
                .name(serverName)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
