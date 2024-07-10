package ru.verstache.gabella.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.verstache.gabella.model.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayerRepository extends JpaRepository<Player, UUID> {

    Optional<Player> findByNick(String nickname);

    @Query("SELECT p FROM Player p ORDER BY p.points DESC")
    List<Player> findTopPlayers(Pageable pageable);
}
