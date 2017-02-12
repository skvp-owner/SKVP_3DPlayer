package org.talh.SKeletonVideoPlayer.gui;

import java.io.File;
import java.io.IOException;

import org.talh.SKeletonVideoPlayer.SKVPIllegalValueException;
import org.talh.SKeletonVideoPlayer.SKVPSyntaxErrorException;
import org.talh.SKeletonVideoPlayer.player.Player3DRenderer;
import org.talh.SKeletonVideoPlayer.player.PlayerBackend;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AppMainContainer extends Application {

	private PlayerBackend playerBackend;
	
	final VBox root = new VBox(8);
	private MenuItem menuItemExit;
	private MenuItem menuItemOpen;
	private MenuItem menuItemHelp;
	private MenuItem menuItemAbout;
	private Stage stage;
	private File lastVisitedDir = new File(System.getProperty("user.home"));
	private Button buttonStop;
	private Button buttonPause;
	private Button buttonPlay;
	private Player3DRenderer playerRenderer;
	private Scene scene;
	private File playedFile;

	private HBox controlsPane;

	private Spinner<Double> spinnerCameraX;

	private Spinner<Double> spinnerCameraY;

	private Spinner<Double> spinnerCameraZ;

	private Spinner<Double> spinnerCameraAngleX;

	private Spinner<Double> spinnerCameraAngleY;

	private Spinner<Double> spinnerCameraAngleZ;
	
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		stage.setOnCloseRequest(e -> System.exit(0));
		initGUI();
		playerBackend = new PlayerBackend();
		playerBackend.setPlayerRenderer(playerRenderer);
	}
	
	private void initGUI() {		
		createMainWindow();
		createMenus();
		createGraphicsPane();
		controlsPane = new HBox(8); 
		root.getChildren().add(controlsPane);
		createVideoPlayBasicControlsPane();
		createCameraControlsPane();
		setupEvents();
		stage.show();
	}

	private void createCameraControlsPane() {
		//VBox cameraControls = new VBox(8);
		//HBox locationControls = new HBox(8);
		//HBox angleControls = new HBox(8);
		//cameraControls.getChildren().addAll(locationControls, angleControls);
		GridPane cameraControls = new GridPane();
		spinnerCameraX = new Spinner<Double>(-999999999.0, 999999999.0, 0.0, 0.1);
		spinnerCameraY = new Spinner<Double>(-999999999.0, 999999999.0, 0.0, 0.1);
		spinnerCameraZ = new Spinner<Double>(-999999999.0, 999999999.0, 0.0, 0.1);
	//	spinnerCameraX.setEditable(true);
	//	spinnerCameraY.setEditable(true);
	//	spinnerCameraZ.setEditable(true);
		//locationControls.getChildren().addAll(new Label("X:"), spinnerCameraX,
			//								new Label("Y:"), spinnerCameraY,
			//								new Label("Z:"), spinnerCameraZ);
		
		spinnerCameraAngleX = new Spinner<Double>(-180.0, 180.0, 0.0, 5.0);
		spinnerCameraAngleY = new Spinner<Double>(-180.0, 180.0, 0.0, 5.0);
		spinnerCameraAngleZ = new Spinner<Double>(-180.0, 180.0, 0.0, 5.0);
	//	spinnerCameraAngleX.setEditable(true);
	//	spinnerCameraAngleY.setEditable(true);
	//	spinnerCameraAngleZ.setEditable(true);
	//	angleControls.getChildren().addAll(new Label("Angle X:"), spinnerCameraAngleX,
		//									new Label("Angle Y:"), spinnerCameraAngleY,
			//								new Label("Angle Z:"), spinnerCameraAngleZ);
		
		cameraControls.add(new Label("Location X:"), 1, 1);
		cameraControls.add(spinnerCameraX, 2, 1);
		cameraControls.add(new Label("    "), 3, 1);
		cameraControls.add(new Label("Location Y:"), 4, 1);
		cameraControls.add(spinnerCameraY, 5, 1);
		cameraControls.add(new Label("    "), 6, 1);
		cameraControls.add(new Label("Location Z:"), 7, 1);
		cameraControls.add(spinnerCameraZ, 8, 1);
		cameraControls.add(new Label("Rotation X:"), 1, 2);
		cameraControls.add(spinnerCameraAngleX, 2, 2);
		cameraControls.add(new Label("Rotation Y:"), 4, 2);
		cameraControls.add(spinnerCameraAngleY, 5, 2);
		cameraControls.add(new Label("Rotation Z:"), 7, 2);
		cameraControls.add(spinnerCameraAngleZ, 8, 2);
		
		
		
		controlsPane.getChildren().add(cameraControls);
		
		//dspinner.set
		//1.5, 3.5, 1.5, 0.5);

	}

	private void createGraphicsPane() {
		playerRenderer = new Player3DRenderer(stage, 0, 0, -5.0, 0, 0, 0);
		//playerRenderer.prefHeightProperty().bind(stage.heightProperty());
		root.getChildren().add(playerRenderer);
	}

	private void createVideoPlayBasicControlsPane() {
		HBox panel = new HBox(8);
		Image imageBtnStop = new Image(getClass().getResourceAsStream("resources/icons/stop_button.png"));
		Image imageBtnPause = new Image(getClass().getResourceAsStream("resources/icons/pause_button.png"));
		Image imageBtnPlay = new Image(getClass().getResourceAsStream("resources/icons/play_button.png"));
		buttonStop = new Button();
		buttonStop.setGraphic(new ImageView(imageBtnStop));
		buttonPause = new Button();
		buttonPause.setGraphic(new ImageView(imageBtnPause));
		buttonPlay = new Button();
		buttonPlay.setGraphic(new ImageView(imageBtnPlay));
		panel.getChildren().add(buttonStop);
		panel.getChildren().add(buttonPause);
		panel.getChildren().add(buttonPlay);
		controlsPane.getChildren().add(panel);
	}

	private void setupEvents() {
		menuItemExit.setOnAction((ActionEvent event) -> {
			exitApplication();
		});
		menuItemOpen.setOnAction((ActionEvent event) -> {
			chooseAndOpenVideoFile();
		});
		buttonPlay.setOnAction((ActionEvent event) -> {
			playerBackend.play(null);
		});
		buttonStop.setOnAction((ActionEvent event) -> {
			playerBackend.stop();
			loadFile(playedFile);
		});
		buttonPause.setOnAction((ActionEvent event) -> {
			playerBackend.pause();
		});
		ChangeListener<Double> cameraSpinnersChangeListener = new ChangeListener<Double>() {
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				playerRenderer.changeCameraLocation(spinnerCameraX.getValue(), 
						spinnerCameraY.getValue(),
						spinnerCameraZ.getValue());	
				
			}			
		};
		spinnerCameraX.valueProperty().addListener(cameraSpinnersChangeListener);
		spinnerCameraY.valueProperty().addListener(cameraSpinnersChangeListener);
		spinnerCameraZ.valueProperty().addListener(cameraSpinnersChangeListener);
		ChangeListener<Double> cameraAngleSpinnersChangeListener = new ChangeListener<Double>() {
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				playerRenderer.changeCameraAngle(spinnerCameraAngleX.getValue(), 
						spinnerCameraAngleY.getValue(),
						spinnerCameraAngleZ.getValue());	
				
			}			
		};
		spinnerCameraAngleX.valueProperty().addListener(cameraAngleSpinnersChangeListener);
		spinnerCameraAngleY.valueProperty().addListener(cameraAngleSpinnersChangeListener);
		spinnerCameraAngleZ.valueProperty().addListener(cameraAngleSpinnersChangeListener);
	}

	private void chooseAndOpenVideoFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open SKVP 3D Video File...");
		fileChooser.setInitialDirectory(lastVisitedDir);                 
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SKVP", "*.skvp"));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));
		playedFile = fileChooser.showOpenDialog(stage);
		if (playedFile == null) {
			return;
		}
		lastVisitedDir  = playedFile.getParentFile();
		loadFile(playedFile);
		playerBackend.play(null);
	}
	
	private void loadFile(File file) {
		try {
			playerBackend.loadFile(file, spinnerCameraX, spinnerCameraY, spinnerCameraZ,
									spinnerCameraAngleX, spinnerCameraAngleY, spinnerCameraAngleZ);
		} catch (SKVPSyntaxErrorException e) {
			showErrorDialog("Error loading selected file...", "Loaded file has syntax errors", e.getMessage());
		} catch (IOException e) {
			showErrorDialog("Error loading selected file...", "Error while reading from file", e.getMessage());
		} catch (SKVPIllegalValueException e) {
			showErrorDialog("Error loading selected file...", "Loaded file contains illegal values", e.getMessage());
		}	
	}
	
	private void showErrorDialog(String title, String errorHeader, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(errorHeader);
		alert.setContentText(content);

		alert.showAndWait();
	}

	private void exitApplication() {
		System.exit(0);		
	}

	private void createMenus() {
		MenuBar mbar = new MenuBar();
		//mbar.setal
		mbar.prefWidthProperty().bind(stage.widthProperty());
		Menu menuFile = new Menu("File");
		mbar.getMenus().add(menuFile);
		Menu menuHelp = new Menu("Help");
		mbar.getMenus().add(menuHelp);
		menuItemOpen = new MenuItem("Open");
		menuItemExit = new MenuItem("Exit");
		menuFile.getItems().add(menuItemOpen);
		menuFile.getItems().add(new SeparatorMenuItem());
		menuFile.getItems().add(menuItemExit);
		
		menuItemHelp = new MenuItem("Help");
		menuItemAbout = new MenuItem("About");
		menuHelp.getItems().add(menuItemHelp);
		menuHelp.getItems().add(new SeparatorMenuItem());
		menuHelp.getItems().add(menuItemAbout);
		
		root.getChildren().add(mbar);	
	}

	private void createMainWindow() {
		scene = new Scene(root, 1024, 768);
		scene.setFill(Color.GRAY);
		stage.setTitle("SKVP 3D Player (SKeleton Video Player)");
		stage.setScene(scene);		
	}

	public static void main(String[] args) {
		launch(args);
	}

}
