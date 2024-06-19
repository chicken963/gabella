package ru.verstache.gabella.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.verstache.gabella.model.Match;
import ru.verstache.gabella.model.Server;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

public interface MatchRepository extends JpaRepository<Match, UUID> {

    Collection<Match> findAllByServerAndFinishedAtAndStartedAt(Server server, LocalDateTime startedAt, LocalDateTime finishedAt);

    Integer countMatchByServer(Server server);

    Collection<Match> findAllByServer(Server server);

}
