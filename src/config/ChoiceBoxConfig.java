package config;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;



public class ChoiceBoxConfig extends ChoiceBox {

	
	public ChoiceBoxConfig(String toolTipString, ObservableList<String> choices, int startingPos, int width, int height) {
		
		this.setTooltip(new Tooltip(toolTipString));
		this.setItems(choices);
		this.getSelectionModel().select(startingPos);
		this.setMinSize(width, height);
		this.setMaxSize(width, height);
	}
}
