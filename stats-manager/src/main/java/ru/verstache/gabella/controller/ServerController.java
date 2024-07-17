package ru.verstache.gabella.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.verstache.gabella.dto.ServerDto;
import ru.verstache.gabella.model.stats.ServerStats;
import ru.verstache.gabella.service.ServerService;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/servers")
@RequiredArgsConstructor
public class ServerController {

    private final ServerService serverService;

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
        return ResponseEntity.ok(serverService.findById(serverId));
    }
}
