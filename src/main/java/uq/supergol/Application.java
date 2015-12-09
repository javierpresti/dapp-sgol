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
	public CommandLineRunner demo(TeamRepository teamRepository, 
			PlayerRepository playerRepository, MatchRepository matchRepository,
			RoundRepository roundRepository, LeagueRepository leagueRepository) {
		return (args) -> {
			teamRepository.save(new Team());
			teamRepository.save(new Team());
			teamRepository.save(new Team());
			teamRepository.save(new Team());
			
			playerRepository.save(new Player("Jorge", Position.Defender));
			playerRepository.save(new Player("Pablo", Position.Forward));
			playerRepository.save(new Player("Tomas", Position.GoalKeeper));

		};
	}
    
}
