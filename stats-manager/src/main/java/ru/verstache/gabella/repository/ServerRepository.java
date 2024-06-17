package ru.verstache.gabella.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.verstache.gabella.model.Server;

import java.util.Optional;
import java.util.UUID;

public interface ServerRepository extends JpaRepository<Server, UUID> {
    Optional<Server> findByName(String serverName);
}
