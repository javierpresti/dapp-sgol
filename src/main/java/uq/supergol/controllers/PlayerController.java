package uq.supergol.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import uq.supergol.model.Player;
import uq.supergol.repositories.PlayerRepository;
import uq.supergol.repositories.TeamRepository;

@RestController
@RequestMapping("/players")
public class PlayerController extends BaseController {
	
	@Autowired
	PlayerController(PlayerRepository playerRepository, TeamRepository teamRepository) {
		super(playerRepository, teamRepository);
	}

	@RequestMapping(value = "/{playerId}", method = RequestMethod.GET)
	Player readPlayer(@PathVariable Long playerId) {
		return getPlayer(playerId);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	Collection<Player> readPlayers() {
		return playerRepository.findAll();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<?> addPlayer(@RequestBody Player input) {
		return getResponseEntity(savePlayer(input));
	}
	
	@RequestMapping(value = "/{playerId}/points", method = RequestMethod.POST)
	ResponseEntity<?> addPointsOfRound(@PathVariable Long playerId, @RequestBody int points) {
		return getResponseEntity(savePlayer(getPlayer(playerId).addPointsOfRound(points)));
	}
	
}
