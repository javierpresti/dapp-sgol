package uq.supergol.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import uq.supergol.model.Player;
import uq.supergol.repositories.LeagueRepository;
import uq.supergol.repositories.MatchRepository;
import uq.supergol.repositories.PlayerRepository;
import uq.supergol.repositories.RoundRepository;
import uq.supergol.repositories.TeamRepository;

@RestController
@RequestMapping("/players")
public class PlayerController extends BaseController {
	
	@Autowired
	PlayerController(PlayerRepository playerRepository, TeamRepository teamRepository,
			MatchRepository matchRepository, RoundRepository roundRepository,
			LeagueRepository leagueRepository) {
		super(playerRepository, teamRepository, matchRepository, roundRepository,
				leagueRepository);
	}

	@RequestMapping(value = "/{playerId}", method = RequestMethod.GET)
	Player readPlayer(@PathVariable Long playerId) {
		return getPlayer(playerId);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	Collection<Player> readPlayers() {
		return getPlayers();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<?> addPlayer(@RequestBody Player input) {
		return getResponseEntity(savePlayer(input), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{playerId}/points", method = RequestMethod.POST)
	ResponseEntity<?> addPoints(@PathVariable Long playerId, @RequestBody int points) {
		return getResponseEntity(savePlayer(getPlayer(playerId).setPoints(points)),
				HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{playerId}/goals", method = RequestMethod.POST)
	ResponseEntity<?> addGoals(@PathVariable Long playerId, @RequestBody int goals) {
		return getResponseEntity(savePlayer(getPlayer(playerId).setGoals(goals)), HttpStatus.OK);
	}
	
}
