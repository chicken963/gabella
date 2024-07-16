package ru.verstache.gabella.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.verstache.gabella.dto.PlayerDto;
import ru.verstache.gabella.dto.ServerDto;
import ru.verstache.gabella.exception.ItemNotFoundException;
import ru.verstache.gabella.mapper.PlayerMapper;
import ru.verstache.gabella.mapper.ServerMapper;
import ru.verstache.gabella.model.Match;
import ru.verstache.gabella.model.Server;
import ru.verstache.gabella.model.stats.ServerStats;
import ru.verstache.gabella.repository.MatchRepository;
import ru.verstache.gabella.repository.ServerRepository;
import ru.verstache.gabella.service.MatchWinnerService;
import ru.verstache.gabella.service.ServerService;

import java.time.LocalDate;
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
    private final MatchWinnerService matchWinnerService;

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
    public ServerStats getServerStats(UUID serverId) {
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new ItemNotFoundException("No server found by id " + serverId));
        return ServerStats.builder()
                .name(server.getName())
                .createdAt(server.getCreatedAt())
                .lastPlayedAt(findLastPlayedTime(server))
                .totalPlayedMatches(matchRepository.countMatchByServer(server))
                .averageMatchesPerDay(getAverageMatchesPerDay(server))
                .maxMatchesPerDay(getMaxMatchesPerDay(server))
                .mostPlayedDay(getMostPlayedDay(server))
                .bestPlayer(findBestPlayer(server))
                .build();
    }

    private LocalDateTime findLastPlayedTime(Server server) {
        return matchRepository.findAllByServer(server).stream()
                .map(Match::getFinishedAt)
                .max(Comparator.comparing(LocalDateTime::toLocalTime))
                .orElse(LocalDateTime.of(1, 1, 1, 0, 0));
    }

    @Override
    public ServerDto findById(UUID serverId) {
        return serverRepository.findById(serverId)
                .map(serverMapper::toDto)
                .orElseThrow(() -> new ItemNotFoundException("No server found by id " + serverId));
    }

    @Override
    public List<ServerDto> findMostPopularServers(Integer amount) {
        PageRequest pageRequest = PageRequest.of(0, amount);
        return matchRepository.findMostPopularServers(pageRequest).stream()
                .map(serverMapper::toDto)
                .collect(Collectors.toList());
    }

    private double getAverageMatchesPerDay(Server server) {
       return matchRepository.findAllByServer(server)
                .stream()
                .collect(Collectors.groupingBy(match -> match.getFinishedAt().toLocalDate(), Collectors.counting()))
                .values().stream()
                .collect(Collectors.summarizingInt(Long::intValue))
                .getAverage();
    }

    private int getMaxMatchesPerDay(Server server) {
        return matchRepository.findAllByServer(server)
                .stream()
                .collect(Collectors.groupingBy(match -> match.getFinishedAt().toLocalDate(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(Map.Entry::getValue)
                .map(Long::intValue)
                .findFirst()
                .orElse(0);
    }

    private LocalDate getMostPlayedDay(Server server) {
        return matchRepository.findAllByServer(server)
                .stream()
                .collect(Collectors.groupingBy(match -> match.getFinishedAt().toLocalDate(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(LocalDate.of(1, 1, 1));
    }

    private PlayerDto findBestPlayer(Server server) {
        return matchRepository.findAllByServer(server)
                .stream()
                .map(matchWinnerService::getWinners)
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
