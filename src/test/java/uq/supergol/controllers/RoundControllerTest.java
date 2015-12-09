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
import uq.supergol.model.matches.Match;
import uq.supergol.model.matches.Round;

@RunWith(SpringJUnit4ClassRunner.class)
public class RoundControllerTest extends BaseControllerTest {

	@Test
	public void RoundNotFound() throws Exception {
		getMockMvc().perform(get("/rounds/1"))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void readSingleRound() throws Exception {
		Round round = addRound();
		getMockMvc().perform(get("/rounds/" + round.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(getContentType()))
				.andExpect(jsonPath("$.id").value(round.getId().intValue()));
	}

	@Test
	public void readRounds() throws Exception {
		Round round0 = addRound();
		Round round1 = addRound();

		getMockMvc().perform(get("/rounds"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(getContentType()))
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].id").value(round0.getId().intValue()))
				.andExpect(jsonPath("$[1].id").value(round1.getId().intValue()));
	}

	@Test
	public void createRound() throws Exception {
		getMockMvc().perform(post("/rounds")
				.contentType(getContentType())
				.content(json(new Round())))
				.andExpect(status().isCreated());
	}
	
	@Test
	public void addMatchToRound() throws Exception {
		Team team1 = addTeam();
		Team team2 = addTeam();
		Match match = addMatch(team1, team2);
		Round round = addRound();
		getMockMvc().perform(post("/rounds/" + round.getId() + "/match")
				.contentType(getContentType())
				.content(String.valueOf(match.getId())))
				.andExpect(status().isOk());
		Assert.assertTrue(roundContainsMatch(round.getId(), match.getId()));
	}

}
