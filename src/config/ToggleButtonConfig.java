package config;

import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyCombination.Modifier;



public class ToggleButtonConfig extends ToggleButton {
	
	
	public ToggleButtonConfig(String buttonText, int width, int height) {
		
		super(buttonText);
		
		this.setMinSize(width, height);
		this.setMaxSize(width, height);
	}
}


