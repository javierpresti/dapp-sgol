package uq.supergol.controllers;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import uq.supergol.Application;
import uq.supergol.model.BaseEntity;
import uq.supergol.model.Player;
import uq.supergol.model.Position;
import uq.supergol.model.Team;
import uq.supergol.model.matches.League;
import uq.supergol.model.matches.Match;
import uq.supergol.model.matches.Round;
import uq.supergol.repositories.LeagueRepository;
import uq.supergol.repositories.MatchRepository;
import uq.supergol.repositories.PlayerRepository;
import uq.supergol.repositories.RoundRepository;
import uq.supergol.repositories.TeamRepository;

@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class BaseControllerTest {

	protected MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(),
			Charset.forName("utf8"));

	protected MockMvc mockMvc;

	@SuppressWarnings("rawtypes")
	protected HttpMessageConverter mappingJackson2HttpMessageConverter;

	@Autowired protected TeamRepository teamRepository;
	@Autowired protected PlayerRepository playerRepository;
	@Autowired protected MatchRepository matchRepository;
	@Autowired protected RoundRepository roundRepository;
	@Autowired protected LeagueRepository leagueRepository;

	@Autowired
	protected WebApplicationContext webApplicationContext;


	@Autowired
	protected void setConverters(HttpMessageConverter<?>[] converters) {
		mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
				hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

		Assert.assertNotNull("the JSON message converter must not be null",
				mappingJackson2HttpMessageConverter);
	}

	@Before
	public void setup() throws Exception {
		mockMvc = webAppContextSetup(webApplicationContext).build();
	}
	
	@After
	public void teardown() throws Exception {
		leagueRepository.deleteAllInBatch();
		roundRepository.deleteAllInBatch();
		matchRepository.deleteAllInBatch();
		teamRepository.deleteAllInBatch();
		playerRepository.deleteAll();
	}
	
	@SuppressWarnings("unchecked")
	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		mappingJackson2HttpMessageConverter.write(
				o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}
	
	protected boolean teamContainsPlayer(long teamId, long playerId) {
		return containsEntity(getTeam(teamId).getPlayers(), playerId);
	}
	
	protected boolean roundContainsMatch(long roundId, long matchId) {
		return containsEntity(getRound(roundId).getMatches(), matchId);
	}
	
	protected boolean leagueContainsRound(long leagueId, long roundId) {
		return containsEntity(getLeague(leagueId).getRounds(), roundId);
	}
	
	protected boolean leagueContainsTeam(long leagueId, long teamId) {
		return containsEntity(getLeague(leagueId).getTeams(), teamId);
	}
	
	protected <E extends BaseEntity> boolean containsEntity(Collection<E> col, long entityId) {
		boolean contains = false;
		for (BaseEntity entity : col) {
			if (entity.getId() == entityId) {
				contains = true;
				break;
			}
		}
		return contains;
	}

	protected boolean teamContainsCaptain(long teamId, long playerId) {
		return getTeam(teamId).getCaptain().getId() == playerId;
	}
	
	protected int getTeamPoints(long teamId) {
		return getTeam(teamId).getRoundPoints();
	}
	
	protected List<Integer> getPlayerPoints(long playerId) {
		return getPlayer(playerId).getPoints();
	}

	protected Team addTeam() {
		return teamRepository.save(new Team("team"));
	}
	protected Player addPlayer(String name, Position pos, String realTeam) {
		return playerRepository.save(new Player(name, pos, realTeam));
	}
	protected Match addMatch(Team team1, Team team2) {
		return matchRepository.save(new Match(team1, team2));
	}
	protected Round addRound() {
		return roundRepository.save(new Round());
	}
	protected League addLeague(String name, int minTeams, int maxTeams) {
		return leagueRepository.save(new League(name, minTeams, maxTeams));
	}
	
	protected Team getTeam(long id)	{	return teamRepository.findOne(id);	}
	protected Player getPlayer(long id){	return playerRepository.findOne(id);	}
	protected Match getMatch(long id)	{	return matchRepository.findOne(id);	}
	protected Round getRound(long id)	{	return roundRepository.findOne(id);	}
	protected League getLeague(long id){	return leagueRepository.findOne(id);	}
	
	public MediaType getContentType()	{	return contentType;	}
	public MockMvc getMockMvc()	{	return mockMvc;	}
	
}
