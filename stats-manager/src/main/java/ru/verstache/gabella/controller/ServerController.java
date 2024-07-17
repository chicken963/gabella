package ru.verstache.gabella.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.verstache.gabella.dto.MatchDto;
import ru.verstache.gabella.dto.ServerDto;
import ru.verstache.gabella.model.stats.ServerStats;
import ru.verstache.gabella.service.MatchService;
import ru.verstache.gabella.service.ServerService;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/servers")
@RequiredArgsConstructor
public class ServerController {

    private final ServerService serverService;
    private final MatchService matchService;

    @Operation(summary = "Get general info about all servers")
    @GetMapping("/info")
    public ResponseEntity<Set<ServerDto>> getServersInfo() {
        return ResponseEntity.ok(serverService.findAll());
    }

    @Operation(summary = "Get stats about specific server")
    @GetMapping("/{id}/stats")
    public ResponseEntity<ServerStats> getServerStats(@PathVariable("id") UUID serverId) {
        return ResponseEntity.ok(serverService.getServerStats(serverId));
    }

    @Operation(summary = "Get info about specific server")
    @GetMapping("/{id}/info")
    public ResponseEntity<ServerDto> getServerInfo(@PathVariable("id") UUID serverId) {
        return ResponseEntity.ok(serverService.findDtoById(serverId));
    }

    @Operation(summary = "Get all matches played on a server at specific date")
    @GetMapping("/{id}/matches/{date}")
    public ResponseEntity<List<MatchDto>> getServerMatchesByDate(@PathVariable("id") UUID serverId,
                                                                 @PathVariable("date")
                                                                 @Parameter(name = "date",
                                                                            example = "2024-07-19")
                                                                 @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                                 LocalDate date) {
        return ResponseEntity.ok(matchService.findByServerIdAndDate(serverId, date));
    }
}
