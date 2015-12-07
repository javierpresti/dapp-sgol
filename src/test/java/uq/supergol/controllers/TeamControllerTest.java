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

    private Team team;

    private List<Player> playerList = new ArrayList<>();

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

        this.team = teamRepository.save(new Team());
        teamRepository.save(new Team());
        teamRepository.save(new Team());
        this.playerList.add(playerRepository.save(new Player("Jorge", Position.Defender)));
        this.playerList.add(playerRepository.save(new Player("Julio", Position.GoalKeeper)));
    }

//    @Test
//    public void teamNotFound() throws Exception {
//        mockMvc.perform(post("/george/bookmarks/")
//                .content(this.json(new Bookmark()))
//                .contentType(contentType))
//                .andExpect(status().isNotFound());
//    }

    @Test
    public void readSingleTeam() throws Exception {
        mockMvc.perform(get("/team/" + this.team.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
        		.andExpect(jsonPath("$.id").value(this.team.getId().intValue()));
    }

//    @Test
//    public void readSingleBookmark() throws Exception {
//    	mockMvc.perform(get("/" + userName + "/bookmarks/"
//    			+ this.bookmarkList.get(0).getId()))
//    	.andExpect(status().isOk())
//    	.andExpect(content().contentType(contentType))
//    	.andExpect(jsonPath("$.id", is(this.bookmarkList.get(0).getId().intValue())))
//    	.andExpect(jsonPath("$.uri", is("http://bookmark.com/1/" + userName)))
//    	.andExpect(jsonPath("$.description", is("A description")));
//    }

    @Test
    public void readTeams() throws Exception {
        mockMvc.perform(get("/team"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$").isArray());
//                .andExpect(jsonPath("$[0].id", is(this.bookmarkList.get(0).getId().intValue())))
//                .andExpect(jsonPath("$[0].uri", is("http://bookmark.com/1/" + userName)))
//                .andExpect(jsonPath("$[0].description", is("A description")))
//                .andExpect(jsonPath("$[1].id", is(this.bookmarkList.get(1).getId().intValue())))
//                .andExpect(jsonPath("$[1].uri", is("http://bookmark.com/2/" + userName)))
//                .andExpect(jsonPath("$[1].description", is("A description")));
    }

    @Test
    public void createTeam() throws Exception {
        String teamJson = json(new Team());
        this.mockMvc.perform(post("/team")
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
