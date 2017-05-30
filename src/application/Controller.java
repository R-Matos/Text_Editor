package application;

import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;

public class Controller {
	
	
	/**
	 * Prompts user to confirm they would like to exit the program via alert.
	 *
	 * @since 1.0
	 */
	public static void exit() {

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Exit");
		alert.setHeaderText("Confirm");
		alert.setContentText("Would you like to exit the program?");
		alert.getButtonTypes().clear(); 											//Removes default buttons
		alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
		alert.showAndWait();

		if (alert.getResult() == ButtonType.YES) {
			System.exit(0);
		}

	}
	
	
	
	/**
	 * Boldens text.
	 *
	 * @since 1.0
	 */
	public static void setBold() {
		
		Boolean isNotBold = Main.getBoldBtn().isSelected();
		
		if (isNotBold) {
			Main.getTextArea().setStyle("-fx-font-weight: bold;");
		} else {
			Main.getTextArea().setStyle("-fx-font-weight: normal;");
		}
	}
	
	
	/**
	 * Italices text.
	 *
	 * @since 1.0
	 * TODO: Fix italics
	 */
	public static void setItalics() {

		Boolean isNotItalics = Main.getItalicsBtn().isSelected();
		
		if (isNotItalics) {
			Main.getTextArea().setStyle("-fx-font-style: italic;");			
		} else {
			Main.getTextArea().setStyle("-fx-font-style: normal;");
		}
	}

	
	/**
	 * Underlines text.
	 *
	 * @since 1.0
	 * TODO: Fix underlines
	 */
	public static void setUnderline() {

		Boolean isNotUnderlined = Main.getUnderlineBtn().isSelected();

		if (isNotUnderlined) {
			System.out.println("test");
			Main.getTextArea().setStyle("-fx-underline: true");
		} else {
			Main.getTextArea().setStyle("-fx-underline: false");
		}
	}
	
	
	/**
	 * Sets text font family.;
	 *
	 * @since 1.0
	 */
	public static void setFont(String font) {
		Main.getTextArea().setStyle("-fx-font-family: " + font);
	}
	
	
	/**
	 * Sets text size.
	 * @since 1.0
	 */
	public static void setSize(int size) {
		Main.getTextArea().setStyle("-fx-font-size: " + size + "pt");
	}
	
	
	/**
	 * Updates text history for undo and redo function.
	 *
	 * @since 1.0
	 */
	public static void updateHistory() {
		
		String rawText;
		int lastWordIndex;
		String lastWord;
		
		rawText = Main.getTextArea().getText();
		rawText.trim();															//Removes last space
		lastWordIndex = rawText.lastIndexOf(" ");
		
		lastWord = " " + rawText.substring(lastWordIndex+1);
		Main.getHistory().add(lastWord);	
	}
	
	
	/**
	 * Undo one word at a time.
	 * 
	 * @since 1.0
	 */
	public static void undo() {
		
		String newText = "";
		int indexOfLastWord = Main.getHistory().size() - 1;
		
		Main.getUndoHistory().add(Main.getHistory().get(indexOfLastWord));		//Word to be removed save to for redo
		Main.getHistory().remove(indexOfLastWord);								//Removes last word
		
		//System.out.println("word to add ["+Main.getUndoHistory().get(indexOfLastWord)+"]");
		
		for (String word : Main.getHistory()) {
			newText = newText.concat(word);
		}
		
		Main.getTextArea().clear();
		Main.getTextArea().setText(newText);
	}
	
	
	/**
	 * Redo one word at a time.
	 * 
	 * @since 1.0
	 */
	public static void redo() {
		
		String newText = "";
		String lastWord = "";
		int indexOfLastWord;
		
		indexOfLastWord = Main.getUndoHistory().size() - 1;
		lastWord = Main.getUndoHistory().get(indexOfLastWord);
		
		System.out.println("["+lastWord+"]");
		
		for (String word : Main.getHistory()) {
			newText = newText.concat(word);
		}
		
		newText = newText.concat(lastWord);
		
		Main.getTextArea().clear();
		Main.getTextArea().setText(newText);
		
		Main.getHistory().add(lastWord);										//Word to be added back is added to history for undo
		Main.getUndoHistory().remove(indexOfLastWord);
	}
	
	
		
	
	/**
	 * Loads text to text editor a line at a time.
	 *
	 * @param  lines contains text from opened file. Each element is a line
	 * @since 1.0
	 */
	public static void loadText(ArrayList<String> lines) {
		
		Main.getTextArea().clear();
		
		for (String line : lines) {
			Main.getTextArea().appendText(line);
			Main.getTextArea().appendText("\n");											//TODO: Windows may require \r
		}
	}
	
	

}
