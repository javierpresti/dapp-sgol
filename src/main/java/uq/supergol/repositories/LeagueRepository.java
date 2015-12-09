package uq.supergol.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import uq.supergol.model.Player;
import uq.supergol.model.matches.League;

public interface LeagueRepository extends JpaRepository<League, Long> {
	
	Optional<Player> findByName(String name);
	
}
