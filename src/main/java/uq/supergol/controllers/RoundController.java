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

import uq.supergol.model.matches.Round;
import uq.supergol.repositories.LeagueRepository;
import uq.supergol.repositories.MatchRepository;
import uq.supergol.repositories.PlayerRepository;
import uq.supergol.repositories.RoundRepository;
import uq.supergol.repositories.TeamRepository;

@RestController
@RequestMapping("/rounds")
public class RoundController extends BaseController {

	@Autowired
	RoundController(PlayerRepository playerRepository, TeamRepository teamRepository,
			MatchRepository matchRepository, RoundRepository roundRepository,
			LeagueRepository leagueRepository) {
		super(playerRepository, teamRepository, matchRepository, roundRepository,
				leagueRepository);
	}

	@RequestMapping(value = "/{roundId}", method = RequestMethod.GET)
	Round readRound(@PathVariable Long roundId) {
		return getRound(roundId);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	Collection<Round> readRounds() {
		return getRounds();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<?> addRound(@RequestBody Round input) {
		return getResponseEntity(saveRound(input), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{roundId}/match", method = RequestMethod.POST)
	ResponseEntity<?> addMatch(@PathVariable Long roundId, @RequestBody int matchId) {
		return getResponseEntity(saveRound(getRound(roundId).addMatch(getMatch(matchId))),
				HttpStatus.OK);
	}

}
