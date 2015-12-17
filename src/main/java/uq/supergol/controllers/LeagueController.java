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

import uq.supergol.model.matches.League;
import uq.supergol.repositories.LeagueRepository;
import uq.supergol.repositories.MatchRepository;
import uq.supergol.repositories.PlayerRepository;
import uq.supergol.repositories.RoundRepository;
import uq.supergol.repositories.TeamRepository;

@RestController
@RequestMapping("/leagues")
public class LeagueController extends BaseController {

	@Autowired
	LeagueController(PlayerRepository playerRepository, TeamRepository teamRepository,
			MatchRepository matchRepository, RoundRepository roundRepository,
			LeagueRepository leagueRepository) {
		super(playerRepository, teamRepository, matchRepository, roundRepository, 
				leagueRepository);
	}

	@RequestMapping(value = "/{leagueId}", method = RequestMethod.GET)
	League readLeague(@PathVariable Long leagueId) {
		return getLeague(leagueId);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	Collection<League> readLeagues() {
		return getLeagues();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<?> addLeague(@RequestBody League input) {
		return getResponseEntity(saveLeague(input), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{leagueId}/round", method = RequestMethod.POST)
	ResponseEntity<?> addRound(@PathVariable Long leagueId, @RequestBody long roundId) {
		return getResponseEntity(saveLeague(getLeague(leagueId).addRound(getRound(roundId))),
				HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{leagueId}/team", method = RequestMethod.POST)
	ResponseEntity<?> addTeam(@PathVariable Long leagueId, @RequestBody long teamId) {
		return getResponseEntity(saveLeague(getLeague(leagueId).addTeam(getTeam(teamId))),
				HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{leagueId}/init", method = RequestMethod.POST)
	ResponseEntity<?> init(@PathVariable Long leagueId) {
		return getResponseEntity(saveLeague(getLeague(leagueId).init()), HttpStatus.OK);
	}

}
