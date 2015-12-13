package uq.supergol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import uq.supergol.model.Player;
import uq.supergol.model.Position;
import uq.supergol.model.Team;
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
			playerRepository.save(player1);
			playerRepository.save(player2);
			playerRepository.save(player3);

			Team team1 = new Team("team1");
			team1.addPlayer(player1);
			team1.addPlayer(player2);

			teamRepository.save(team1);
			teamRepository.save(new Team("team2"));
			teamRepository.save(new Team("team3"));
			teamRepository.save(new Team("team4"));

		};
	}

}
