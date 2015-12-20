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
import uq.supergol.model.Position;
import uq.supergol.model.Team;
import uq.supergol.repositories.LeagueRepository;
import uq.supergol.repositories.MatchRepository;
import uq.supergol.repositories.PlayerRepository;
import uq.supergol.repositories.RoundRepository;
import uq.supergol.repositories.TeamRepository;

@RestController
@RequestMapping("/teams")
public class TeamController extends BaseController {
	
	@Autowired
	TeamController(PlayerRepository playerRepository, TeamRepository teamRepository,
			MatchRepository matchRepository, RoundRepository roundRepository,
			LeagueRepository leagueRepository) {
		super(playerRepository, teamRepository, matchRepository, roundRepository,
				leagueRepository);
	}
		
	@RequestMapping(value = "/{teamId}", method = RequestMethod.GET)
	Team readTeam(@PathVariable Long teamId) {
		return getTeam(teamId);
	}

	@RequestMapping(method = RequestMethod.GET)
	Collection<Team> readTeams() {
		return getTeams();
	}
	
	@RequestMapping(value = "/ready", method = RequestMethod.GET)
	Collection<Team> readReadyTeams() {
		Collection<Team> teams = getTeams();
		teams.removeIf(team -> {return !team.isReady();});
		return teams;
	}
	
	@RequestMapping(value = "/{teamId}/position/{position}", method = RequestMethod.GET)
	Collection<Player> readPlayersOfPosition(@PathVariable Long teamId, 
			@PathVariable String position) {
		Team team = getTeam(teamId);
		Collection<Player> players = playerRepository.findByPosition(Position.valueOf(position));
		players.removeIf(player -> { return !team.canAddPlayer(player); });
		return players;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<?> addTeam(@RequestBody Team input) {
		return getResponseEntity(saveTeam(input), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{teamId}/name", method = RequestMethod.POST)
	ResponseEntity<?> setName(@PathVariable long teamId, @RequestBody String name) {
		return getResponseEntity(saveTeam(getTeam(teamId).setName(name)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{teamId}/totalpoints", method = RequestMethod.POST)
	ResponseEntity<?> addPoints(@PathVariable long teamId, @RequestBody int points) {
		return getResponseEntity(saveTeam(getTeam(teamId).setTotalPoints(points)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{teamId}/player", method = RequestMethod.POST)
	ResponseEntity<?> addPlayer(@PathVariable long teamId, @RequestBody long playerId) {
		return getResponseEntity(saveTeam(getTeam(teamId).addPlayer(getPlayer(playerId))), 
				HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{teamId}/playerremove", method = RequestMethod.POST)
	ResponseEntity<?> removePlayer(@PathVariable long teamId, @RequestBody long playerId) {
		return getResponseEntity(saveTeam(getTeam(teamId).removePlayer(getPlayer(playerId))), 
				HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{teamId}/captain", method = RequestMethod.POST)
	ResponseEntity<?> addCaptain(@PathVariable long teamId, @RequestBody long captainId) {
		return getResponseEntity(saveTeam(getTeam(teamId).setCaptain(getPlayer(captainId))), 
				HttpStatus.OK);
	}
	
}
