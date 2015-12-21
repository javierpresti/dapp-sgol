package uq.supergol.data;

import org.joda.time.DateTime;

import uq.supergol.model.Player;
import uq.supergol.model.Position;
import uq.supergol.repositories.PlayerRepository;

public class UpdateCSVReader extends CSVReader {

	protected PlayerRepository playerRepository;
	protected long id;
	
	public UpdateCSVReader(PlayerRepository playerRepository) {
		this.playerRepository = playerRepository;
	}
	
	@Override
	protected String csvFileRoute() {
		return rootPath + "update.csv";
	}

	@Override
	protected void manageHeader(String[] header) {	
		id = getId(header);
	}

	@Override
	protected Player manageRow(String[] row) {
		Player player = playerRepository.getOne(getId(row));
		Position pos = player.getPosition();
		if (!pos.equals(getPosition(row))) {
			throw new RuntimeException("Invalid position for id");
		}
		player.setGoals(getGoals(row));
		return player;
	}
	
	protected long getId(String[] row)				{ return Long.valueOf(row[0]);	}
	protected Position getPosition(String[] row)	{ return Position.valueOf(row[1]);	}
	protected int getGoals(String[] row)			{ return Integer.valueOf(row[2]);	}

}
