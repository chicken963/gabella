package ru.verstache.gabella.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.verstache.gabella.model.MatchWinner;
import ru.verstache.gabella.model.MatchWinnerId;

import java.util.List;
import java.util.UUID;

@Repository
public interface MatchWinnerRepository extends JpaRepository<MatchWinner, MatchWinnerId> {

//    List<MatchWinner> findByMatchIdAndWinnerId(UUID matchId, UUID winnerId);

}
