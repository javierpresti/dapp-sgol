package uq.supergol.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uq.supergol.model.Player;

public abstract class CSVReader {
	
	protected String rootPath = "src//main//resources//CSVs//";
	protected String valuesDivision = ";";

	public List<Player> readFile() throws IOException{
		return readFile(new File(csvFileRoute()));
	}
	
	public List<Player> readFile(File file) throws IOException{
		List<Player> players = new ArrayList<>();
		String line;
		BufferedReader reader = new BufferedReader(new FileReader(file));
		manageHeader(reader.readLine().split(valuesDivision));
		while ((line = reader.readLine()) != null) {
			String[] values = line.split(valuesDivision);
			players.add(manageRow(values));
		}
		reader.close();
		return players;
	}
	
	protected abstract String csvFileRoute();
	protected abstract void manageHeader(String[] header);
	protected abstract Player manageRow(String[] rowsValues);	

}
