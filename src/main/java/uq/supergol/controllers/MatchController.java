package uq.supergol.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uq.supergol.model.matches.Match;
import uq.supergol.repositories.LeagueRepository;
import uq.supergol.repositories.MatchRepository;
import uq.supergol.repositories.PlayerRepository;
import uq.supergol.repositories.RoundRepository;
import uq.supergol.repositories.TeamRepository;

@RestController
@RequestMapping("/matches")
public class MatchController extends BaseController {

	@Autowired
	MatchController(PlayerRepository playerRepository, TeamRepository teamRepository,
			MatchRepository matchRepository, RoundRepository roundRepository,
			LeagueRepository leagueRepository) {
		super(playerRepository, teamRepository, matchRepository, roundRepository,
				leagueRepository);
	}

	@RequestMapping(value = "/{matchId}", method = RequestMethod.GET)
	Match readMatch(@PathVariable Long matchId) {
		return getMatch(matchId);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	Collection<Match> readMatches() {
		return getMatches();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<?> addMatch(@RequestBody Match input) {
		return getResponseEntity(saveMatch(input), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{matchId}/points", method = RequestMethod.POST)
	ResponseEntity<?> setScore(@PathVariable Long matchId,
			@RequestParam(value="score0") int score0, @RequestParam(value="score1") int score1) {
		return getResponseEntity(saveMatch(getMatch(matchId).setScore(score0, score1)),
				HttpStatus.OK);
	}

}
