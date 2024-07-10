package ru.verstache.gabella.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.UUID;

@Data
@Embeddable
@EqualsAndHashCode
public class MatchWinnerId implements Serializable {
    @JoinColumn(name = "match_id")
    private UUID matchId;
    @JoinColumn(name = "winner_id")
    private UUID winnerId;
}
