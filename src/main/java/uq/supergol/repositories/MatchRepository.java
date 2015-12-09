package uq.supergol.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import uq.supergol.model.matches.Match;

public interface MatchRepository extends JpaRepository<Match, Long> {

}
