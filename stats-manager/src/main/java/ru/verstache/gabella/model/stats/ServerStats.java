package ru.verstache.gabella.model.stats;

import lombok.Builder;
import lombok.Data;
import ru.verstache.gabella.dto.PlayerDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class ServerStats {
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime lastPlayedAt;
    private int totalPlayedMatches;
    private double averageMatchesPerDay;
    private double maxMatchesPerDay;
    private LocalDate mostPlayedDay;
    private PlayerDto bestPlayer;
}
