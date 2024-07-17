package ru.verstache.gabella.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.verstache.gabella.model.Match;
import ru.verstache.gabella.model.Server;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MatchRepository extends JpaRepository<Match, UUID> {

    List<Match> findAllByServerAndStartedAtAndFinishedAt(Server server, LocalDateTime startedAt, LocalDateTime finishedAt);

    Integer countMatchByServer(Server server);

    List<Match> findAllByServer(Server server);

    List<Match> findAllByServerAndFinishedAtBetween(Server server, LocalDateTime dayStart, LocalDateTime dayEnd);

    @Query("SELECT m FROM Match m ORDER BY m.finishedAt DESC")
    List<Match> findLastFinished(Pageable pageable);

    @Query(value = "SELECT s FROM Server s JOIN Match m ON s.id = m.server.id GROUP BY s.id ORDER BY COUNT(m.id) DESC")
    List<Server> findMostPopularServers(Pageable pageable);

}
