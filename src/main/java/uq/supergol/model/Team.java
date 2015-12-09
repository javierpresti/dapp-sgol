package uq.supergol.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Team extends BaseEntity {

	protected String name;
	protected int roundPoints;
	@ManyToOne
	protected Player captain;
	@ManyToMany(mappedBy = "teams", fetch = FetchType.EAGER)
	protected Set<Player> players = new HashSet<Player>();
	
	public Team() {}

	public Team addPlayer(Player player) {
		if (canAddPlayer(player)) getPlayers().add(player.addTeam(this));
		return this;
	}
	
	public Team removePlayer(Player player) {
		getPlayers().remove(player.removeTeam(this));
		return this;
	}
	
	public Team addPoints(int points) {
		this.roundPoints += points;
		return this;
	}
	
	public boolean teamisReady() {
		return getCaptain() != null && getPlayers().size() == 11;
	}
	
	private boolean canAddPlayer(Player player) {
		boolean canAdd = getPlayers().size() < 11;
		if (canAdd) {
			Position position = player.getPosition();
			canAdd = playersOfPosition(position) <= position.maxPlayersPerTeam;
		}
		return canAdd;
	}
	
	private int playersOfPosition(Position position) {
		int qty = 0;
		
		for (Player player : getPlayers()) {
			if (player.position.equals(position)) {
				qty++;
			}
		}
		return qty;
	}
	
	@Override
	public String toString() {
		return toString(", points=%d", getRoundPoints());
	}
	
	public Team setCaptain(Player player) {
		if (player!= null && !getPlayers().contains(player)) {
			throw new RuntimeException("captain must be a player of the team");
		}
		this.captain = player;
		return this;
	}
	
	public String getName()				{	return name;	}
	public int getRoundPoints() 		{	return roundPoints;	}
	public Set<Player> getPlayers()		{	return players;		}
	public Player getCaptain()			{	return captain;		}
	
	public void setName(String name)	{	this.name = name;	}

}
