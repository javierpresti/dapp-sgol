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

@RunWith(SpringJUnit4ClassRunner.class)
public class MatchControllerTest extends BaseControllerTest {
	
	@Test
	public void MatchNotFound() throws Exception {
		getMockMvc().perform(get("/matches/1"))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void readSingleMatch() throws Exception {
		Match match = addMatch(addTeam(), addTeam());
		getMockMvc().perform(get("/matches/" + match.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(getContentType()))
				.andExpect(jsonPath("$.id").value(match.getId().intValue()));
	}

	@Test
	public void readMatches() throws Exception {
		Match match0 = addMatch(addTeam(), addTeam());
		Match match1 = addMatch(addTeam(), addTeam());

		getMockMvc().perform(get("/matches"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(getContentType()))
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].id").value(match0.getId().intValue()))
				.andExpect(jsonPath("$[1].id").value(match1.getId().intValue()));
	}

	@Test
	public void createMatch() throws Exception {
		getMockMvc().perform(post("/matches")
				.contentType(getContentType())
				.content(json(new Match(addTeam(), addTeam()))))
				.andExpect(status().isCreated());
	}
	
	@Test
	public void setScore() throws Exception {
		Team team1 = addTeam();
		Team team2 = addTeam();
		Match match = addMatch(team1, team2);
		int score0 = 2;
		int score1 = 1;
		String uri = String.format("/matches/%d/points?score0=%d&score1=%d", 
				match.getId(), score0, score1);
		getMockMvc().perform(post(uri))
				.andExpect(status().isOk());
		match = getMatch(match.getId());
		Assert.assertEquals(score0, match.getScore0());
		Assert.assertEquals(score1, match.getScore1());
	}
	
}
