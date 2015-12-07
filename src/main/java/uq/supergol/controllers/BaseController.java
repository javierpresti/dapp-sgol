package uq.supergol.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import uq.supergol.model.BaseEntity;
import uq.supergol.model.Player;
import uq.supergol.model.Team;
import uq.supergol.repositories.PlayerRepository;
import uq.supergol.repositories.TeamRepository;

public abstract class BaseController {
	
	protected final PlayerRepository playerRepository;
	protected final TeamRepository teamRepository;
	
	@Autowired
	BaseController(PlayerRepository playerRepository, TeamRepository teamRepository) {
		this.playerRepository = playerRepository;
		this.teamRepository = teamRepository;
	}
	
	protected ResponseEntity<?> getResponseEntity(BaseEntity entity) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(entity.getId()).toUri());
		return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
	}
	
	protected Player getPlayer(long playerId) {
		Player player = playerRepository.findOne(playerId);
		if (player == null) throw new NotFoundException(playerId);
		return player;
	}
	
	protected Player savePlayer(Player player) {
		return playerRepository.save(player);
	}
	
	protected Team getTeam(long teamId) {
		Team team = teamRepository.findOne(teamId);
		if (team == null) throw new NotFoundException(teamId);
		return team;
	}
	
	protected Team saveTeam(Team team) {
		return teamRepository.save(team);
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	class NotFoundException extends RuntimeException {

		private static final long serialVersionUID = -6635602726156831996L;

		public NotFoundException(long teamId) {
			super("could not find entity '" + teamId + "'.");
		}
	}

}
