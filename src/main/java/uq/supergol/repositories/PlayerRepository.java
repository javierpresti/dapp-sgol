package uq.supergol.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import uq.supergol.model.Player;
import uq.supergol.model.Position;

public interface PlayerRepository extends JpaRepository<Player, Long> {

	Collection<Player> findByName(String name);
	Collection<Player> findByRealTeam(String realTeam);
	Collection<Player> findByPosition(Position position);

}
