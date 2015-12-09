package uq.supergol.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import uq.supergol.model.matches.Round;

public interface RoundRepository extends JpaRepository<Round, Long> {

}
