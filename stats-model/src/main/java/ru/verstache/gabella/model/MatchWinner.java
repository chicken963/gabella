package ru.verstache.gabella.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "matches_winners")
public class MatchWinner implements Serializable {

    @EmbeddedId
    private MatchWinnerId id;

    @ManyToOne
    @MapsId("matchId")
    @JoinColumn(name = "match_id")
    private Match match;

    @ManyToOne
    @MapsId("winnerId")
    @JoinColumn(name = "winner_id")
    private Player winner;

    @Column(nullable=false)
    private Integer points;

}
