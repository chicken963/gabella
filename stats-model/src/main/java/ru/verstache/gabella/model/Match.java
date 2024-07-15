package ru.verstache.gabella.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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

    @OneToMany(mappedBy = "match", fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    private Set<MatchWinner> matchWinners = new HashSet<>();

}
