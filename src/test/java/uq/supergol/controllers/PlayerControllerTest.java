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

import uq.supergol.model.Player;
import uq.supergol.model.Position;

@RunWith(SpringJUnit4ClassRunner.class)
public class PlayerControllerTest extends BaseControllerTest {
	
	@Test
	public void playerNotFound() throws Exception {
		getMockMvc().perform(get("/players/1"))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void readSinglePlayer() throws Exception {
		Player player = addPlayer("Jorge", Position.Defender);
		getMockMvc().perform(get("/players/" + player.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(getContentType()))
				.andExpect(jsonPath("$.id").value(player.getId().intValue()));
	}

	@Test
	public void readPlayers() throws Exception {
		Player player0 = addPlayer("Jorge", Position.GoalKeeper);
		Player player1 = addPlayer("Julio", Position.Forward);

		getMockMvc().perform(get("/players"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(getContentType()))
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].id").value(player0.getId().intValue()))
				.andExpect(jsonPath("$[1].id").value(player1.getId().intValue()));
	}

	@Test
	public void createPlayer() throws Exception {
		getMockMvc().perform(post("/players")
				.contentType(getContentType())
				.content(json(new Player("Jorge", Position.Defender))))
				.andExpect(status().isCreated());
	}
	
	@Test
	public void addPointsOfRound() throws Exception {
		Player player = addPlayer("Jaime", Position.Midfielder);
		Assert.assertTrue(player.getPointsPerRound().isEmpty());
		Integer pointsToAdd = 3;
		getMockMvc().perform(post("/players/" + player.getId() + "/points")
				.content(String.valueOf(pointsToAdd))
				.contentType(getContentType()))
				.andExpect(status().isOk());
		Assert.assertEquals(pointsToAdd, getPlayerPoints(player.getId()).get(0));

		Integer newPointsToAdd = 1;
		getMockMvc().perform(post("/players/" + player.getId() + "/points")
				.content(String.valueOf(newPointsToAdd))
				.contentType(getContentType()))
				.andExpect(status().isOk());
		Assert.assertEquals(newPointsToAdd, getPlayerPoints(player.getId()).get(1));
	}

}
