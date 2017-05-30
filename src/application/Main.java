package application;
	
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import config.ButtonConfig;
import config.ChoiceBoxConfig;
import config.ToggleButtonConfig;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
* The Text_Editor launches a simple text editor.
*
* @author  Ricardo Matos
* @version 1.0
* @since   30-05-2017
*/

public class Main extends Application {
		
	private static MenuItem newDoc;
	private static MenuItem open;
	private static MenuItem save;
	private static MenuItem saveAs;
	private static MenuItem exit;
	
	private static ChoiceBoxConfig fontCb;
	private static ChoiceBoxConfig sizeCb;
	
	private static ToggleButtonConfig boldBtn;
	private static ToggleButtonConfig italicsBtn;
	private static ToggleButtonConfig underlineBtn;
	
	private static ButtonConfig clearBtn;
	private static ButtonConfig undoBtn;
	private static ButtonConfig redoBtn;
	
	private static TextArea textArea;
	
	
	private static String filePath = "";													//file
	
	private static ArrayList<String> history = new ArrayList<String>();			//Used for undo
	private static ArrayList<String> undoHistory = new ArrayList<String>();		//Used for redo
	
	private static boolean shouldUpdate = false;
	private static boolean mustUpdate = true;									//Used to determine whether user undo's the last written without post whitespace. 
																					//Therefore need to update as it's only done after a whitespace.
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	
	@Override
	public void start(Stage primaryStage) {
		
		
		
		try {
			
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//Main layout setups
			BorderPane root = new BorderPane();	

			HBox topPanel = new HBox();
			topPanel.setSpacing(5);
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//File menu
			newDoc = new MenuItem("New Document");
			open = new MenuItem("Open");
			save = new MenuItem("Save");
			saveAs = new MenuItem("Save as");
			exit = new MenuItem("Exit");
			
			newDoc.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
			open.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
			save.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
			exit.setAccelerator(KeyCombination.keyCombination("Ctrl+W"));
			
			Menu menuFile = new Menu("File");
			menuFile.getItems().addAll(newDoc, open, new SeparatorMenuItem(), save, saveAs, new SeparatorMenuItem(), exit);
			
			MenuBar menuBar = new MenuBar();
			menuBar.setMinSize(60, 30);
			menuBar.setMaxSize(60, 30);
			menuBar.getMenus().addAll(menuFile);		
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// Top bar
			fontCb = new ChoiceBoxConfig(
					"Select the font family", 
					FXCollections.observableArrayList("Times", "Helvetica", "Zapf-Chancery", "Western", "Courier"), 
					0, 120, 30);
			
			sizeCb = new ChoiceBoxConfig(
					"Select the font size", 
					FXCollections.observableArrayList("6","7","8","9","10","11","12","13","14"), 
					5, 50, 30);

			boldBtn = new ToggleButtonConfig("Bold", 60, 30);
			italicsBtn = new ToggleButtonConfig("Italics", 60, 30);
			underlineBtn = new ToggleButtonConfig("Underline", 60, 30);
			
			clearBtn = new ButtonConfig("Clear", 60, 30);
			undoBtn = new ButtonConfig("Undo", 60, 30);
			redoBtn = new ButtonConfig("Redo", 60, 30);
			
			textArea = new TextArea();
			textArea.setStyle("" 
			   + "-fx-font-weight: normal;"
			   + "-fx-font-style: normal;"
			   + "-fx-font-size: 11pt;"					
			);
			
			
			topPanel.getChildren().addAll(menuBar, new Separator(), fontCb, sizeCb, new Separator(), boldBtn,
					italicsBtn, underlineBtn, new Separator(), clearBtn, undoBtn, redoBtn);
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//Roots and stage
			root.setTop(topPanel);
			root.setCenter(textArea);
			root.setStyle("-fx-font-family: Courier");;
			
			Scene scene = new Scene(root,800,800);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			
			setupEvents(primaryStage);
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Getters
	public static TextArea getTextArea() {
		return textArea;
	}
	
	public static ToggleButton getBoldBtn() {
		return boldBtn;
	}
	
	public static ToggleButton getItalicsBtn() {
		return italicsBtn;
	}
	
	public static ToggleButton getUnderlineBtn() {
		return underlineBtn;
	}
	
	public static ArrayList<String> getHistory() {
		return history;
	}
	
	public static ArrayList<String> getUndoHistory() {
		return undoHistory;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	private static void setupEvents(Stage primaryStage) {
		
		
		primaryStage.setOnCloseRequest(e -> { 
			e.consume();
			Controller.exit();
		});
		
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//Open file
		open.setOnAction(e -> {

			FileChooser fileChooser = new FileChooser();

			//TODO: finish filters
			//FileChooser.ExtensionFilter winFilter = new FileChooser.ExtensionFilter("Text", "*.txt"); // Windows
			//FileChooser.ExtensionFilter winFilter = new FileChooser.ExtensionFilter("Rich text format", "*.rtf"); // Windows
			//FileChooser.ExtensionFilter unixFilter = new
			//FileChooser.ExtensionFilter("text", "*."); //Unix 			TODO: // figure out extension
			//fileChooser.getExtensionFilters().add(winFilter);

			File file = fileChooser.showOpenDialog(null);
		
			if (file != null) {
				filePath = file.getPath();
				ReadFile readFile = new ReadFile(filePath);
				
				try {
					ArrayList<String> textLines = readFile.openFile();
					Controller.loadText(textLines);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//Save file
		save.setOnAction(e -> {
			e.consume();
			
			//Checks if filepath been set, done via opening file. If not launches save as.
			if (filePath.isEmpty()) {
				saveAs.fire();
				return;
			}
			
			WriteFile writeFile = new WriteFile(filePath);
			
			try {
				writeFile.writeToFile(textArea.getText());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//SaveAs File
		saveAs.setOnAction(e -> {
			e.consume();
	        
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select directory to save file.");
			
			File file = fileChooser.showSaveDialog(null);
			
			if(file != null) {
				String path = file.getPath();
				WriteFile writeFile = new WriteFile(path);
				
				try {
					writeFile.writeToFile(textArea.getText());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		fontCb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
		      public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
		        String selection = (String) fontCb.getItems().get((Integer) number2);
		        Controller.setFont(selection);
		      }
		    });
		
		sizeCb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
		      public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
		        String selection = (String) sizeCb.getItems().get((Integer) number2);
		        int selectionInt = Integer.parseInt(selection);
		        Controller.setSize(selectionInt);
		      }
		    });
		
		
		exit.setOnAction(e -> {
			e.consume();
			Controller.exit();
		});
		
		
		boldBtn.setOnAction(e -> {
			e.consume();
			Controller.setBold();				
		});
		
		boldBtn.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.B, KeyCombination.SHORTCUT_DOWN),
				new Runnable() {
					@Override
					public void run() {
						boldBtn.fire();
					}
				});
		
		
		italicsBtn.setOnAction(e -> {
			e.consume();
			Controller.setItalics();
		});
		
		italicsBtn.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.I, KeyCombination.SHORTCUT_DOWN),
				new Runnable() {
					@Override
					public void run() {
						italicsBtn.fire();
					}
				});
		
		
		underlineBtn.setOnAction(e -> {
			e.consume();
			Controller.setUnderline();
		});
		
		underlineBtn.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.U, KeyCombination.SHORTCUT_DOWN),
				new Runnable() {
					@Override
					public void run() {
						underlineBtn.fire();
					}
				});
		
		
		clearBtn.setOnAction(e -> {
			e.consume();
            textArea.clear();
        });
		

		undoBtn.setOnAction(e -> {
			e.consume();
			
			if (mustUpdate) {															//Fixes a bug somehow
				Controller.updateHistory();
			}
			
            Controller.undo();
            
            mustUpdate = false;
        });
		
		undoBtn.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_DOWN),
				new Runnable() {
					@Override
					public void run() {
						undoBtn.fire();
					}
				});
		
		
		redoBtn.setOnAction(e -> {
			e.consume();
            Controller.redo();
        });
		
		redoBtn.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN),
				new Runnable() {
					@Override
					public void run() {
						redoBtn.fire();
					}
				});
		
		
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//Used for undo and redo.
		textArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
            	
            	if (event.getCode().isWhitespaceKey() && shouldUpdate) 
            		Controller.updateHistory();
            		shouldUpdate = false;												//Prevents saving multiple whitespace
            	
            	if (event.getCode().isLetterKey() || event.getCode().isDigitKey())		//Any key producing text
            		shouldUpdate = true;
            		mustUpdate = true;
            }
        });
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
