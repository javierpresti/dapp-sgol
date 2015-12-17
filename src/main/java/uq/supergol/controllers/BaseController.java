package uq.supergol.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import uq.supergol.model.BaseEntity;
import uq.supergol.model.Player;
import uq.supergol.model.Team;
import uq.supergol.model.matches.League;
import uq.supergol.model.matches.Match;
import uq.supergol.model.matches.Round;
import uq.supergol.repositories.LeagueRepository;
import uq.supergol.repositories.MatchRepository;
import uq.supergol.repositories.PlayerRepository;
import uq.supergol.repositories.RoundRepository;
import uq.supergol.repositories.TeamRepository;

public abstract class BaseController {
	
	protected final PlayerRepository playerRepository;
	protected final TeamRepository teamRepository;
	protected final MatchRepository matchRepository;
	protected final RoundRepository roundRepository;
	protected final LeagueRepository leagueRepository;
	
	@Autowired
	BaseController(PlayerRepository playerRepository, TeamRepository teamRepository,
			MatchRepository matchRepository, RoundRepository roundRepository,
			LeagueRepository leagueRepository) {
		this.playerRepository = playerRepository;
		this.teamRepository = teamRepository;
		this.matchRepository = matchRepository;
		this.roundRepository = roundRepository;
		this.leagueRepository = leagueRepository;
	}
	
	protected ResponseEntity<?> getResponseEntity(BaseEntity entity, HttpStatus status) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(entity.getId()).toUri());
		return new ResponseEntity<>(null, httpHeaders, status);
	}
	
	protected Player savePlayer(Player player)	{	return playerRepository.save(player);	}
	protected Team saveTeam(Team team)			{	return teamRepository.save(team);	}
	protected Match saveMatch(Match match)		{	return matchRepository.save(match);	}
	protected Round saveRound(Round round)		{	return roundRepository.save(round);	}
	protected League saveLeague(League league)	{	return leagueRepository.save(league);	}

	protected Collection<Player> getPlayers()	{	return playerRepository.findAll();	}
	protected Collection<Team> getTeams()		{	return teamRepository.findAll();	}
	protected Collection<Match> getMatches()	{	return matchRepository.findAll();	}
	protected Collection<Round> getRounds()	{	return roundRepository.findAll();	}
	protected Collection<League> getLeagues()	{	return leagueRepository.findAll();	}
	
	protected Player getPlayer(long id){	return getEntity(playerRepository, id);}
	protected Team getTeam(long id)	{	return getEntity(teamRepository, id);	}
	protected Match getMatch(long id)	{	return getEntity(matchRepository, id);	}
	protected Round getRound(long id)	{	return getEntity(roundRepository, id);	}
	protected League getLeague(long id){	return getEntity(leagueRepository, id);	}
	
	protected <E extends BaseEntity, R extends JpaRepository<E, Long>> E getEntity(
			R repository, long entityId) {
		E entity = repository.findOne(entityId);
		if (entity == null) throw new NotFoundException(entityId);
		return entity;
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	class setEditItemNotFoundException extends RuntimeException {

		private static final long serialVersionUID = -6635602726156831996L;

		public NotFoundException(long teamId) {
			super("could not find entity '" + teamId + "'.");
		}
	}

}
