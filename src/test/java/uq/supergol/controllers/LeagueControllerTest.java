package uq.supergol.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uq.supergol.model.Team;
import uq.supergol.model.matches.League;
import uq.supergol.model.matches.Round;

@RunWith(SpringJUnit4ClassRunner.class)
public class LeagueControllerTest extends BaseControllerTest {
	
	@Test
	public void leagueNotFound() throws Exception {
		getMockMvc().perform(get("/leagues/1"))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void readSingleLeague() throws Exception {
		League league = addLeague("L", 8, 16);
		getMockMvc().perform(get("/leagues/" + league.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(getContentType()))
				.andExpect(jsonPath("$.id").value(league.getId().intValue()));
	}

	@Test
	public void readLeagues() throws Exception {
		League league0 = addLeague("L", 8, 16);
		League league1 = addLeague("J", 8, 16);

		getMockMvc().perform(get("/leagues"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(getContentType()))
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].id").value(league0.getId().intValue()))
				.andExpect(jsonPath("$[1].id").value(league1.getId().intValue()));
	}

	@Test
	public void createLeague() throws Exception {
		String name = "Leaguie";
		int minTeams = 8, maxTeams = 12;
		League league = new League(name, minTeams, maxTeams);
		getMockMvc().perform(post("/leagues")
				.contentType(getContentType())
				.content(json(league)))
				.andExpect(status().isCreated());
	}

	@Test
	public void addRoundToLeague() throws Exception {
		League league = addLeague("L", 8, 16);
		Round round = addRound();
		getMockMvc().perform(post("/leagues/" + league.getId() + "/round")
				.content(String.valueOf(round.getId()))
				.contentType(getContentType()))
				.andExpect(status().isOk());
		Assert.assertTrue(leagueContainsRound(league.getId(), round.getId()));
	}
	
	@Test
	public void addTeamToLeague() throws Exception {
		League league = addLeague("L", 8, 16);
		Team team = addTeam();
		getMockMvc().perform(post("/leagues/" + league.getId() + "/team")
				.content(String.valueOf(team.getId()))
				.contentType(getContentType()))
		.andExpect(status().isOk());
		Assert.assertTrue(leagueContainsTeam(league.getId(), team.getId()));
	}

}
