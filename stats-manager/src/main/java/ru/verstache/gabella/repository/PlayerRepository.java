package ru.verstache.gabella.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.verstache.gabella.model.Player;

import java.util.Optional;
import java.util.UUID;

public interface PlayerRepository extends JpaRepository<Player, UUID> {

    Optional<Player> findByNick(String nickname);
}
