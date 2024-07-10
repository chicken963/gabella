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
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable=false)
    private String nick;

    @Column(name="registered_at", nullable=false)
    private LocalDateTime registeredAt;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "participants", fetch = FetchType.EAGER)
    private Set<Match> matches;

    @OneToMany(mappedBy = "winner")
    @EqualsAndHashCode.Exclude
    private Set<MatchWinner> matchWinners = new HashSet<>();

    @Column(name="total_matches", nullable=false)
    private Integer totalMatches;

    @Column(name="won_matches", nullable=false)
    private Integer wonMatches;

    @Column(name="points", nullable=false)
    private Integer points;

}
