package ru.verstache.gabella.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.verstache.gabella.aspect.ValidReportListSize;
import ru.verstache.gabella.dto.MatchDto;
import ru.verstache.gabella.dto.PlayerDto;
import ru.verstache.gabella.dto.ServerDto;
import ru.verstache.gabella.service.MatchService;
import ru.verstache.gabella.service.PlayerService;
import ru.verstache.gabella.service.ServerService;

import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final MatchService matchService;
    private final PlayerService playerService;
    private final ServerService serverService;

    @ValidReportListSize
    @GetMapping("/recent-matches/{amount}")
    public ResponseEntity<List<MatchDto>> getLastMatches(@PathVariable("amount") Integer amount) {
        return ResponseEntity.ok(matchService.findLastMatches(amount));
    }

    @ValidReportListSize
    @GetMapping("/best-players/{amount}")
    public ResponseEntity<List<PlayerDto>> getTopPlayers(@PathVariable("amount") Integer amount) {
        return ResponseEntity.ok(playerService.findTopPlayers(amount));
    }

    @ValidReportListSize
    @GetMapping("/popular-servers/{amount}")
    public ResponseEntity<List<ServerDto>> getMostPopularServers(@PathVariable("amount") Integer amount) {
        return ResponseEntity.ok(serverService.findMostPopularServers(amount));
    }
}
