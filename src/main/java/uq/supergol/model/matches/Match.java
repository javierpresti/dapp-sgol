package uq.supergol.model.matches;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import uq.supergol.model.BaseEntity;
import uq.supergol.model.Team;

@Entity
public class Match extends BaseEntity {
	
	@ManyToOne
	protected Team team0;
	@ManyToOne
	protected Team team1;
	
	protected int score0;
	protected int score1;
	
	Match() {}
	
	public Match(Team team0, Team team1) {
		this.team0 = team0;
		this.team1 = team1;
	}
	
	public Team getWinner() {
		return score0 > score1 ? team0 : score0 < score1 ? team1 : null;
	}
	
	public Match setScore(int scoreTeam0, int scoreTeam1) {
		score0 = scoreTeam0;
		score1 = scoreTeam1;
		return this;
	}

	@Override
	public String toString() {
		return toString(", team0=%d, team1=%d", getTeam0().getId(), getTeam1().getId());
	}

	public Team getTeam0()	{	return team0;	}
	public Team getTeam1()	{	return team1;	}
	public int getScore0() {	return score0;	}
	public int getScore1() {	return score1;	}

}
