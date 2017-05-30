package config;

import javafx.scene.control.Button;


public class ButtonConfig extends Button {
	
	public ButtonConfig(String buttonText, int width, int height) {

		super(buttonText);

		this.setMinSize(width, height);
		this.setMaxSize(width, height);
	}
}
