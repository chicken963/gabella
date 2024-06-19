package ru.verstache.gabella.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.verstache.gabella.dto.ServerDto;
import ru.verstache.gabella.model.stats.ServerStats;
import ru.verstache.gabella.service.ServerService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/servers")
@RequiredArgsConstructor
public class ServerController {

    private final ServerService serverService;

    @GetMapping("/info")
    public ResponseEntity<Set<ServerDto>> getServersInfo() {
        return ResponseEntity.ok(serverService.findAll());
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<ServerStats> getServerStats(@PathVariable("id") UUID serverId) {
        return ResponseEntity.ok(serverService.getServersStats(serverId));
    }
}
