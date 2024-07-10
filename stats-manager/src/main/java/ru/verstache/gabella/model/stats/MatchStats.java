package ru.verstache.gabella.model.stats;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class MatchStats {
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private Set<MatchPlayerStats> participants;
    private Set<MatchPlayerStats> winners;

    @Data
    @Builder
    private static class MatchPlayerStats {
        private String nick;
        private Integer points;
    }
}
