package ru.verstache.gabella.model.stats;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PlayerStats  {
    private String nick;
    private LocalDateTime registeredAt;
    private int totalMatches;
    private int wonMatches;
    private int longestWinStreak;
    private int points;
    private String mostOftenTeammate;
    private String mostOftenEnemy;
}
