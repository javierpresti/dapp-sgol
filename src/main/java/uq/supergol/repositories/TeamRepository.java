package uq.supergol.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import uq.supergol.model.Player;
import uq.supergol.model.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {

	Collection<Team> findByRoundPoints(int roundPoints);
	Collection<Team> findByCaptain(Player captain);
	
}
