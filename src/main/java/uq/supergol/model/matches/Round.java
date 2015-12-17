package uq.supergol.model.matches;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import uq.supergol.model.BaseEntity;

@Entity
public class Round extends BaseEntity {
	
	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	protected List<Match> matches = new ArrayList<Match>();
	
	Round() {}
	
	public Round(Match... matches) {
		this.matches.addAll(Arrays.asList(matches));
	}
	
	public Round(List<Match> matches) {
		this.matches = matches;
	}
	
	public Round addMatch(Match match) {
		getMatches().add(match);
		return this;
	}
	
	public List<Match> getMatches()	{	return matches;	}

}
