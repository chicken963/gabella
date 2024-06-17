package ru.verstache.gabella.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @JoinColumn(name="server_id", nullable=false)
    private Server server;


    @Column(name="started_at", nullable=false)
    private LocalDateTime startedAt;


    @Column(name="finished_at", nullable=false)
    private LocalDateTime finishedAt;

    @ManyToMany
    @JoinTable(
            name = "matches_players",
            joinColumns = @JoinColumn(name = "match_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private Set<Player> participants;

    @ManyToMany
    @JoinTable(
            name = "matches_winners",
            joinColumns = @JoinColumn(name = "match_id"),
            inverseJoinColumns = @JoinColumn(name = "winner_id")
    )
    private Set<Player> winners;

}
