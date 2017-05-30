package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javafx.stage.DirectoryChooser;

public class WriteFile {
	
	
	private String path;
	private boolean append = false;								//save = true, save as = false
	
	
	//Constructor
	public WriteFile(String filePath) {
		path = filePath;
		
	}
	
	//Constructor2
	public WriteFile(String filePath, boolean appendFlag) {
		path = filePath;
		append = appendFlag;
	}
	
	
	public void writeToFile(String textLine) throws IOException {
		
		FileWriter write = new FileWriter(path , append);			//Opens file, stores text as bytes
		PrintWriter printLine = new PrintWriter(write);				//Handles byte code to plaintext
			
		printLine.printf("%s" + "%n" , textLine);					//Printf allows formatting. Ensures unix and windows formats are equal. 
																		//%s = any length, %n = new line
		printLine.close();
	}
	
	


	
	
	
	
	
	
	
	
}
