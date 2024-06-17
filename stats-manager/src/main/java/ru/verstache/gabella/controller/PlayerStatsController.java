package ru.verstache.gabella.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.verstache.gabella.model.stats.PlayerStats;
import ru.verstache.gabella.service.PlayerService;

@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerStatsController {

    private final PlayerService playerService;

    @Operation(summary = "Get statistics for player")
    @GetMapping("/{nick}/stats")
    public ResponseEntity<PlayerStats> getPlayerStats(@PathVariable("nick") String nick) {
        return ResponseEntity.ok(playerService.getStats(nick));
    }
}
