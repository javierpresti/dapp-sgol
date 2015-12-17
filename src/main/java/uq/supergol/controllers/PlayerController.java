package uq.supergol.controllers;

import java.util.Collection;
import java.util.List;

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
import uq.supergol.model.matches.League;
import uq.supergol.model.matches.Match;
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
	
	@RequestMapping(value = "/position/{position}", method = RequestMethod.GET)
	Collection<Player> readPlayersOfPosition(@PathVariable String position) {
		return playerRepository.findByPosition(Position.valueOf(position));
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
	
	@RequestMapping(value = "/all", method = RequestMethod.POST)
	ResponseEntity<?> addAll() {
		int index = 0;
		for (Player player: getPlayers()) {
			index = player.getPoints().size() - 1;
			for (Team team : player.getTeams()) {
				team.addPoints(index, player.getPoints().get(index));
				saveTeam(team);
			}
		}
		for (League league : getLeagues()) {
			if (league.getRounds().size() > index) {
				for (Match match : league.getRounds().get(index).getMatches()) {
					Team team0 = getTeam(match.getTeam0().getId());
					Team team1 = getTeam(match.getTeam1().getId());
					match.setScore(team0.getPointsFor(index), team1.getPointsFor(index));
					saveTeam(team0);
					saveTeam(team1);
				}
				saveLeague(league);
			}
		}
		return getResponseEntity(saveLeague(getLeague(1)), HttpStatus.OK);
	}
	
}
