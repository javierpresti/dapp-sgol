package uq.supergol.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Team extends BaseEntity {

	protected int roundPoints;
	@ManyToOne
	protected Player captain;
	@ManyToMany(mappedBy = "teams", fetch = FetchType.EAGER)
	protected Set<Player> players = new HashSet<Player>();
	
	public Team() {}

	public Team addPlayer(Player player) {
		getPlayers().add(player.addTeam(this));
		return this;
	}
	
	public Team setCaptain(Player captain) {
		this.captain = captain;
		return this;
	}
	
	public Team addPoints(int points) {
		this.roundPoints += points;
		return this;
	}
	
	@Override
	public String toString() {
		return toString(", points=%d", getRoundPoints());
	}

	public int getRoundPoints() 		{	return roundPoints;	}
	public Set<Player> getPlayers()		{	return players;		}
	public Player getCaptain()			{	return captain;		}

}
