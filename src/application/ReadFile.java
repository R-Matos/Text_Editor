package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadFile {
	
	
	private String path;
	
	
	
	public ReadFile (String filePath) {
		path = filePath;
	}
	

	
	public ArrayList<String> openFile() throws IOException {

		FileReader file = new FileReader(path);					//Reads bytes of file. char=byte
		BufferedReader buffer = new BufferedReader(file);		//Reads over lines instead of just characters
		
		ArrayList<String> textLines = new ArrayList<String>();
		
		String rndLine;
		while ((rndLine = buffer.readLine()) != null) {
			textLines.add(rndLine);
		}
		
		buffer.close();
		
		return textLines;
	}

}
