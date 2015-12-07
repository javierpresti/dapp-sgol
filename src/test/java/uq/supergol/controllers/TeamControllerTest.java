package uq.supergol.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import uq.supergol.Application;
import uq.supergol.model.Player;
import uq.supergol.model.Position;
import uq.supergol.model.Team;
import uq.supergol.repositories.PlayerRepository;
import uq.supergol.repositories.TeamRepository;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class TeamControllerTest {


    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private List<Team> teams;
    private List<Player> players;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        this.teamRepository.deleteAllInBatch();
        this.playerRepository.deleteAllInBatch();
        
        teams = new ArrayList<>();
        players = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
        	teams.add(teamRepository.save(new Team()));
        }
        players.add(playerRepository.save(new Player("Jorge", Position.Defender)));
        players.add(playerRepository.save(new Player("Julio", Position.GoalKeeper)));
    }

    @Test
    public void playerNotFound() throws Exception {
        mockMvc.perform(post("/teams/1/player")
                .content("15")
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void teamNotFound() throws Exception {
        mockMvc.perform(get("/teams/132"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void readSingleTeam() throws Exception {
    	Team team = teams.get(0);
        mockMvc.perform(get("/teams/" + team.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
        		.andExpect(jsonPath("$.id").value(team.getId().intValue()));
    }

    @Test
    public void readTeams() throws Exception {
        mockMvc.perform(get("/teams"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(teams.get(0).getId().intValue()))
                .andExpect(jsonPath("$[1].id").value(teams.get(1).getId().intValue()))
                .andExpect(jsonPath("$[2].id").value(teams.get(2).getId().intValue()))                
                ;
    }

    @Test
    public void createTeam() throws Exception {
        String teamJson = json(new Team());
        this.mockMvc.perform(post("/teams")
                .contentType(contentType)
                .content(teamJson))
                .andExpect(status().isCreated());
    }

    @SuppressWarnings("unchecked")
	protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
    
}
