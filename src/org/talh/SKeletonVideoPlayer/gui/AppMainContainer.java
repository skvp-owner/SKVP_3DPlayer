package org.talh.SKeletonVideoPlayer.gui;

import java.io.File;
import java.io.IOException;

import org.talh.SKeletonVideoPlayer.SKVPIllegalValueException;
import org.talh.SKeletonVideoPlayer.SKVPSyntaxErrorException;
import org.talh.SKeletonVideoPlayer.gui.VideoPlayerButtonsInterface.ButtonType;
import org.talh.SKeletonVideoPlayer.gui.VideoPlayerCameraControlInterface.ControlType;
import org.talh.SKeletonVideoPlayer.player.Player3DRenderer;
import org.talh.SKeletonVideoPlayer.player.PlayerBackend;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Spinner;
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
	//private Button buttonStop;
	//private Button buttonPause;
	//private Button buttonPlay;
	private Player3DRenderer playerRenderer;
	private Scene scene;
	private File playedFile;

	private HBox controlsPane;

	private VideoPlayerButtonsInterface basicButtons;
	private VideoPlayerCameraControlInterface cameraControls;
	
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
		cameraControls = new VideoPlayerCameraControlPanel();
		//VBox cameraControls = new VBox(8);
		//HBox locationControls = new HBox(8);
		//HBox angleControls = new HBox(8);
		//cameraControls.getChildren().addAll(locationControls, angleControls);
		//GridPane cameraControls = new GridPane();
		//spinnerCameraX = new Spinner<Double>(-999999999.0, 999999999.0, 0.0);
		//spinnerCameraY = new Spinner<Double>(-999999999.0, 999999999.0, 0.0);
		//spinnerCameraZ = new Spinner<Double>(-999999999.0, 999999999.0, 0.0);
	//	spinnerCameraX.setEditable(true);
	//	spinnerCameraY.setEditable(true);
	//	spinnerCameraZ.setEditable(true);
		//locationControls.getChildren().addAll(new Label("X:"), spinnerCameraX,
			//								new Label("Y:"), spinnerCameraY,
			//								new Label("Z:"), spinnerCameraZ);
		
		//spinnerCameraDestinationX = new Spinner<Double>(-999999999.0, 999999999.0, 0);  //(-180.0, 180.0, 0.0, 5.0);
		//spinnerCameraDestinationY = new Spinner<Double>(-999999999.0, 999999999.0, 0);
		//spinnerCameraDestinationZ = new Spinner<Double>(-999999999.0, 999999999.0, 0);
	//	spinnerCameraAngleX.setEditable(true);
	//	spinnerCameraAngleY.setEditable(true);
	//	spinnerCameraAngleZ.setEditable(true);
	//	angleControls.getChildren().addAll(new Label("Angle X:"), spinnerCameraAngleX,
		//									new Label("Angle Y:"), spinnerCameraAngleY,
			//								new Label("Angle Z:"), spinnerCameraAngleZ);
		//spinnerCameraSceneRotation = new Spinner<Double>(-999999999.0, 999999999.0, 0);
		
		
		//cameraControls.add(new Label("Location X:"), 1, 1);
		//cameraControls.add(spinnerCameraX, 2, 1);
		//cameraControls.add(new Label("    "), 3, 1);
		//cameraControls.add(new Label("Location Y:"), 4, 1);
		//cameraControls.add(spinnerCameraY, 5, 1);
		//cameraControls.add(new Label("    "), 6, 1);
		//cameraControls.add(new Label("Location Z:"), 7, 1);
		//cameraControls.add(spinnerCameraZ, 8, 1);
		//cameraControls.add(new Label("Destination X:"), 1, 2);
		//cameraControls.add(spinnerCameraDestinationX, 2, 2);
		//cameraControls.add(new Label("Destination Y:"), 4, 2);
		//cameraControls.add(spinnerCameraDestinationY, 5, 2);
		//cameraControls.add(new Label("Destination Z:"), 7, 2);
		//cameraControls.add(spinnerCameraDestinationZ, 8, 2);
		//cameraControls.add(new Label("Scene Rotation:"), 1, 3);
		//cameraControls.add(spinnerCameraSceneRotation, 2, 3);
		
		
		
		controlsPane.getChildren().add((Node)cameraControls);
		
		//dspinner.set
		//1.5, 3.5, 1.5, 0.5);

	}

	private void createGraphicsPane() {
		playerRenderer = new Player3DRenderer(stage, 0, 0, -5.0, 0, 0, 0, 0);
		//playerRenderer.prefHeightProperty().bind(stage.heightProperty());
		root.getChildren().add(playerRenderer);
	}

	private void createVideoPlayBasicControlsPane() {
		basicButtons = new PlayerBasicButtonsPanel();
		controlsPane.getChildren().add((Node)basicButtons);
	}

	private void setupEvents() {
		menuItemExit.setOnAction((ActionEvent event) -> {
			exitApplication();
		});
		menuItemOpen.setOnAction((ActionEvent event) -> {
			chooseAndOpenVideoFile();
		});
		basicButtons.addVideoPlayerButtonsListener(new VideoPlayerButtonsListener() {			
			@Override
			public void buttonSelected(ButtonType buttonType) {
				switch (buttonType) {
					case PAUSE:
						pauseLoadedVideoAndUpdateControllers();						
						break;
					case PLAY:
						playLoadedVideoAndUpdateControllers();
						break;
					case STOP:
						stopLoadedVideoAndUpdateControllers();						
						break;
					default:
						break;
				}				
			}
		});
		cameraControls.addVideoPlayerCameraControlListener(new VideoPlayerCameraControlListener() {
			
			private void updateCameraLocation() {
				Platform.runLater(()-> {
					playerRenderer.changeCameraLocation(cameraControls.getControllerValue(ControlType.LOCATION_X), 
							cameraControls.getControllerValue(ControlType.LOCATION_Y),
							cameraControls.getControllerValue(ControlType.LOCATION_Z));
				});
			}
			
			private void updateCameraDestination() {
				Platform.runLater(()-> {
					playerRenderer.changeCameraDestination(cameraControls.getControllerValue(ControlType.DESTINATION_X), 
							cameraControls.getControllerValue(ControlType.DESTINATION_Y),
							cameraControls.getControllerValue(ControlType.DESTINATION_Z));
				});
				
			}
			
			private void updateCameraSceneRotation() {
				Platform.runLater(()-> {
					playerRenderer.changeCameraSceneRotation(cameraControls.getControllerValue(ControlType.SCENE_ROTATION));
				});
			}
			
			@Override
			public void controllerValueChanged(ControlType controlType, double value) {
				switch (controlType) {
					case DESTINATION:
						break;
					case DESTINATION_X:
						updateCameraDestination();
						break;
					case DESTINATION_Y:
						updateCameraDestination();
						break;
					case DESTINATION_Z:
						updateCameraDestination();
						break;
					case LOCATION:
						break;
					case LOCATION_X:
						updateCameraLocation();
						break;
					case LOCATION_Y:
						updateCameraLocation();
						break;
					case LOCATION_Z:
						updateCameraLocation();
						break;
					case SCENE_ROTATION:
						updateCameraSceneRotation();
						break;
					case ZOOM:
						break;
					case ZOOM_IN:
						break;
					case ZOOM_OUT:
						break;
					case ZOOM_SCALE:
						break;
					case ZOOM_STEP:
						break;
					default:
						break;	
				}
			}
		});
		/*
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
		ChangeListener<Double> cameraDestinationSpinnersChangeListener = new ChangeListener<Double>() {
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				playerRenderer.changeCameraDestination(spinnerCameraDestinationX.getValue(), 
						spinnerCameraDestinationY.getValue(),
						spinnerCameraDestinationZ.getValue());	
				
			}			
		};
		spinnerCameraDestinationX.valueProperty().addListener(cameraDestinationSpinnersChangeListener);
		spinnerCameraDestinationY.valueProperty().addListener(cameraDestinationSpinnersChangeListener);
		spinnerCameraDestinationZ.valueProperty().addListener(cameraDestinationSpinnersChangeListener);
	
		ChangeListener<Double> cameraSceneRotationSpinnerChangeListener = new ChangeListener<Double>() {
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				playerRenderer.changeCameraSceneRotation(spinnerCameraSceneRotation.getValue());	
			}			
		};
		spinnerCameraSceneRotation.valueProperty().addListener(cameraSceneRotationSpinnerChangeListener);
	*/
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
		playLoadedVideoAndUpdateControllers();
	}
	
	private void playLoadedVideoAndUpdateControllers() {
		playerBackend.play(null);
		ButtonType[] buttonsToEnable = {ButtonType.PAUSE, ButtonType.STOP};
		ButtonType[] buttonsToDisable = {ButtonType.PLAY};
		basicButtons.setButtonsEnabled(buttonsToEnable, true);
		basicButtons.setButtonsEnabled(buttonsToDisable, false);
	}
	
	private void pauseLoadedVideoAndUpdateControllers() {
		playerBackend.pause();
		ButtonType[] buttonsToEnable = {ButtonType.PLAY, ButtonType.STOP};
		ButtonType[] buttonsToDisable = {ButtonType.PAUSE};
		basicButtons.setButtonsEnabled(buttonsToEnable, true);
		basicButtons.setButtonsEnabled(buttonsToDisable, false);
	}
	
	private void stopLoadedVideoAndUpdateControllers() {
		playerBackend.stop();
		loadFile(playedFile);
		ButtonType[] buttonsToEnable = {ButtonType.PLAY};
		ButtonType[] buttonsToDisable = {ButtonType.STOP, ButtonType.PAUSE};
		basicButtons.setButtonsEnabled(buttonsToEnable, true);
		basicButtons.setButtonsEnabled(buttonsToDisable, false);
	}
	
	private void loadFile(File file) {
		try {
			playerBackend.loadFile(file, cameraControls);
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
