package uq.supergol;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import uq.supergol.model.Player;
import uq.supergol.model.Position;
import uq.supergol.model.Team;
import uq.supergol.model.matches.League;
import uq.supergol.repositories.LeagueRepository;
import uq.supergol.repositories.MatchRepository;
import uq.supergol.repositories.PlayerRepository;
import uq.supergol.repositories.RoundRepository;
import uq.supergol.repositories.TeamRepository;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner demo(TeamRepository teamRepository, PlayerRepository playerRepository,
			MatchRepository matchRepository, RoundRepository roundRepository, LeagueRepository leagueRepository) {
		return (args) -> {
			Player player1 = new Player("Jorge", Position.Defender, "Boca");
			Player player2 = new Player("Pablo", Position.Forward, "River");
			Player player3 = new Player("Tomas", Position.GoalKeeper, "Arsenal");
			Player player4 = new Player("Carlos", Position.Forward, "Lanus");
			playerRepository.save(player1);
			playerRepository.save(player2);
			playerRepository.save(player3);
			playerRepository.save(player4);
			
			List<Team> teams = new ArrayList<>();
			for (int i = 1; i < 8; i++) {
				teams.add(new Team("team" + i));
			}

			for (Team team : teams) {
				teamRepository.save(team);
			}
			
			League league1 = new League("liga1", 2, 8);
			League league2 = new League("liga2", 2, 4);
			league1.addTeam(teams.get(0)).addTeam(teams.get(1)).addTeam(teams.get(2)).
					addTeam(teams.get(3));
			league2.addTeam(teams.get(4)).addTeam(teams.get(5));
			leagueRepository.save(league1);
			leagueRepository.save(league2);
		};
	}

}
