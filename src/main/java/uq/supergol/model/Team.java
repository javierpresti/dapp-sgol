package uq.supergol.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Team extends BaseEntity {

	protected String name;
	protected int totalPoints;
	@ElementCollection(fetch = FetchType.EAGER)
	protected List<Integer> points = new ArrayList<Integer>();
	@ManyToOne
	protected Player captain;
	@ManyToMany(mappedBy = "teams", fetch = FetchType.EAGER)
	protected Set<Player> players = new HashSet<Player>();
	
	Team() {}
	
	public Team(String name) {
		this.name = name;
	}

	public Team addPlayer(Player player) {
		if (canAddPlayer(player)) getPlayers().add(player.addTeam(this));
		return this;
	}
	
	public Team removePlayer(Player player) {
		getPlayers().remove(player.removeTeam(this));
		return this;
	}
	
	public Team setTotalPoints(int points) {
		this.totalPoints += points;
		return this;
	}
	
	public Team addPoints(int index, int points) {
		if (index == getPoints().size()) getPoints().add(0);
		getPoints().set(index, getPoints().get(index) + points);
		return this;
	}
	
	public boolean teamisReady() {
		return getCaptain() != null && getPlayers().size() == 11;
	}
	
	private boolean canAddPlayer(Player player) {
		boolean canAdd = getPlayers().size() < 11;
		if (canAdd) {
			Position position = player.getPosition();
			canAdd = playersOfPosition(position) < position.maxPlayersPerTeam;
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
	
	public int getPointsFor(int roundNumber) {
		if (getPoints().size() == roundNumber) {
			getPoints().add(0);
		}
		return getPoints().get(roundNumber);
	}
	
	@Override
	public String toString() {
		return toString(", points=%d", getTotalPoints());
	}
	
	public Team setCaptain(Player player) {
		if (player!= null && !getPlayers().contains(player)) {
			throw new RuntimeException("captain must be a player of the team");
		}
		this.captain = player;
		return this;
	}
	
	public Team setName(String name)	{	this.name = name; return this;	}

	public String getName()				{	return name;	}
	public int getTotalPoints() 		{	return totalPoints;	}
	public List<Integer> getPoints() 	{	return points;	}
	public Set<Player> getPlayers()		{	return players;		}
	public Player getCaptain()			{	return captain;		}
	
}
