package ru.verstache.gabella.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.verstache.gabella.model.Match;
import ru.verstache.gabella.model.Player;
import ru.verstache.gabella.model.Server;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public interface MatchRepository extends JpaRepository<Match, UUID> {

    @Query("SELECT m FROM Match m " +
            "WHERE m.server = :server " +
            "AND m.startedAt = :startedAt " +
            "AND m.finishedAt = :finishedAt ")
    Collection<Match> findExistingDuplicate(@Param("server") Server server,
                                            @Param("startedAt") LocalDateTime startedAt,
                                            @Param("finishedAt") LocalDateTime finishedAt);

}
