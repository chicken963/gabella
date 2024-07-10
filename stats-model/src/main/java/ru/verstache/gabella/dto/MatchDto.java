package ru.verstache.gabella.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record MatchDto(ServerDto server,
                       LocalDateTime startedAt,
                       LocalDateTime finishedAt,
                       Set<PlayerDto> participants,
                       Set<WinnerDto> winners) {
}
