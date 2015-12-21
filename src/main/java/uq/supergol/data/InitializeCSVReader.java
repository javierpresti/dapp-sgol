package uq.supergol.data;

import uq.supergol.model.Player;
import uq.supergol.model.Position;

public class InitializeCSVReader extends CSVReader{

	@Override
	protected String csvFileRoute() {
		return rootPath + "initializePlayers.csv";
	}

	@Override
	protected void manageHeader(String[] header) {	}
	
	@Override
	protected Player manageRow(String[] rowsValues) {
		return new Player(rowsValues[0], Position.valueOf(rowsValues[1]), rowsValues[2]);
	}

}
