package uq.supergol.model.matches;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import uq.supergol.model.BaseEntity;
import uq.supergol.model.Team;

@Entity
public class League extends BaseEntity {
	
	protected String name;
	protected int minTeams;
	protected int maxTeams;
	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	protected List<Round> rounds = new ArrayList<Round>();
	@ManyToMany(fetch = FetchType.EAGER)
	protected Set<Team> teams = new HashSet<Team>();
	
	League() {}
	
	public League(String name, int minTeams, int maxTeams) {
		this.name = name;
		this.minTeams = minTeams;
		this.maxTeams = maxTeams;
	}
	
	public League init() throws RuntimeException {
//		if (!canInit()) {
//			throw new RuntimeException("League can not init");
//		}
//		if (isInited()) {
//			throw new RuntimeException("League has already begun");
//		}
		if (canInit() &&!isInited()) {
			arrangeRounds();
		}
		return this;
	}
	
	protected void arrangeRounds() {
		List<Team> teams = new ArrayList<Team>(getTeams());
		int qtyTeams = teams.size();
		int qtyMatches = (qtyTeams + 1) / 2;
		boolean oddTeams = qtyTeams % 2 != 0;
		int qtyRounds = oddTeams ? qtyTeams : qtyTeams - 1;
        Collections.shuffle(teams, new Random());
        
		for (int round = 0; round < qtyRounds; round++) {
	        List<Match> matches = new ArrayList<Match>(qtyMatches);
			int team0 = oddTeams ? 1 : 0;
			int team1 = qtyTeams - 1;
			if (!oddTeams && round % 2 != 0) {
				matches.add(new Match(teams.get(team1--), teams.get(team0++)));
			}
			while (team0 < qtyMatches) {
				matches.add(new Match(teams.get(team0++), teams.get(team1--)));
			}
			addRound(new Round(matches));
			
			Team lastTeam = teams.get(teams.size() - 1);
			teams.remove(teams.size() - 1);
			teams.add(oddTeams ? 0 : 1, lastTeam);
		}
	}
	
	public boolean isInited() {
		return !getRounds().isEmpty();
	}

	public boolean canInit() {
		return getTeams().size() >= getMinTeams() && getTeams().size() <= getMaxTeams();
	}
	
	protected boolean canAddTeam() {
		return getTeams().size() < getMaxTeams();
	}
	
	public League addRound(Round round)	{	getRounds().add(round); return this;	}
	public League removeTeam(Team team)	{	getTeams().remove(team); return this;	}
	
	public League addTeam(Team team) {
		if (canAddTeam()) {
			getTeams().add(team);
		}
		return this;
	}
	
	public String getName()	{	return name;	}
	public int getMinTeams()	{	return minTeams;	}
	public int getMaxTeams()	{	return maxTeams;	}
	public List<Round> getRounds()	{	return rounds;	}
	public Set<Team> getTeams()	{	return teams;	}

}
