package uq.supergol.model;

public enum Position {

	GoalKeeper(1, 0), Defender(3, 3), Midfielder(4, 1), Forward(3, 1);

	protected final int maxPlayersPerTeam;
	protected final int goalsRatio;

	Position(int maxPlayersPerTeam, int goalsRatio) {
		this.maxPlayersPerTeam = maxPlayersPerTeam;
		this.goalsRatio = goalsRatio;
	}

	public int pointsFor(int goals) {
		switch (this) {
			case GoalKeeper:	return goals == 0 ? 2 : 0;
			default:			return goals * getGoalsRatio();
		}
	}

	public int getMaxPlayersPerTeam()	{ return maxPlayersPerTeam;	}
	public int getGoalsRatio()			{ return goalsRatio;	}

}
