package org.talh.SKeletonVideoPlayer.gui;

import java.io.File;
import java.io.IOException;

import org.talh.SKeletonVideoPlayer.SKVPIllegalValueException;
import org.talh.SKeletonVideoPlayer.SKVPSyntaxErrorException;
import org.talh.SKeletonVideoPlayer.gui.VideoPlayerButtonsInterface.ButtonType;
import org.talh.SKeletonVideoPlayer.gui.VideoPlayerCameraControlInterface.ControlType;
import org.talh.SKeletonVideoPlayer.player.Defs;
import org.talh.SKeletonVideoPlayer.player.Player3DRenderer;
import org.talh.SKeletonVideoPlayer.player.PlayerBackend;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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

	private VBox controlsPane;

	private VideoPlayerButtonsInterface basicButtons;
	private VideoPlayerCameraControlInterface cameraControls;

	private VideoPlayerTimelineInterface timelinePane;

	private HBox buttonsAndTimelinePane;
	
	public File getPlayedFile() {
		return playedFile;
	}
	
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
		controlsPane = new VBox(Defs.BUTTONS_PANEL_BETWEEN_SPACING);
		controlsPane.setPadding(new Insets(Defs.BUTTONS_PANEL_BETWEEN_SPACING));
		root.getChildren().add(controlsPane);
		buttonsAndTimelinePane = new HBox(Defs.BUTTONS_PANEL_BETWEEN_SPACING);
		controlsPane.getChildren().add(buttonsAndTimelinePane);
		createVideoPlayBasicControlsPane();
		createCameraControlsPane();
		createTimelinePane();
		setupEvents();
		stage.sizeToScene();
		stage.show();
	}

	private void createTimelinePane() {
		timelinePane = new VideoPlayerTimelinePanel(this);
		buttonsAndTimelinePane.getChildren().add((Node)timelinePane);
		HBox.setHgrow((Node)timelinePane, Priority.ALWAYS);		
	}

	private void createCameraControlsPane() {
		cameraControls = new VideoPlayerCameraControlPanel();
		cameraControls.setAllControllersEditable(true);
		
		
		controlsPane.getChildren().add((Node)cameraControls);
		
	}

	private void createGraphicsPane() {
		playerRenderer = new Player3DRenderer(stage, 0, 0, -5.0, 0, 0, 0, 0);
		//playerRenderer.prefHeightProperty().bind(stage.heightProperty());
		root.getChildren().add(playerRenderer);
	}

	private void createVideoPlayBasicControlsPane() {
		basicButtons = new PlayerBasicButtonsPanel();
		basicButtons.setButtonEnabled(ButtonType.OPEN, true);
		buttonsAndTimelinePane.getChildren().add((Node)basicButtons);
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
						playLoadedVideoAndUpdateControllers(null, null);
						break;
					case STOP:
						stopLoadedVideoAndUpdateControllers();						
						break;
					case OPEN:
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								chooseAndOpenVideoFile();
							}
						});						
						break;
					default:
						break;
				}				
			}
		});
		
		timelinePane.addVideoPlayerTimelineListener(new VideoPlayerTimelineListener() {
			
			@Override
			public void userModificationRequest() {
				stopLoadedVideoAndUpdateControllers();
			}
			
			@Override
			public void timeJumpRequest(long requestedFrame, Long endFrame) {
				playLoadedVideoAndUpdateControllers(requestedFrame, endFrame);
			}
			
			@Override
			public void playFpsChangeRequest(double fps) {
				playerBackend.setFps(fps);
				
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
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SKVP", "*.skvp", "*.skvp.gz"));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));
		playedFile = fileChooser.showOpenDialog(stage);
		if (playedFile == null) {
			return;
		}
		lastVisitedDir  = playedFile.getParentFile();
		loadFile(playedFile, null, null);
		playLoadedVideoAndUpdateControllers(null, null);
	}
	
	private void playLoadedVideoAndUpdateControllers(Long startFrom, Long endFrame) {
		if (startFrom != null) {
			loadFile(playedFile, startFrom, endFrame);
		}
		playerBackend.play();
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
		loadFile(playedFile, null, null);
		ButtonType[] buttonsToEnable = {ButtonType.PLAY};
		ButtonType[] buttonsToDisable = {ButtonType.STOP, ButtonType.PAUSE};
		basicButtons.setButtonsEnabled(buttonsToEnable, true);
		basicButtons.setButtonsEnabled(buttonsToDisable, false);
	}
	
	private void loadFile(File file, Long startFromFrame, Long endFrame) {
		try {
			playerBackend.loadFile(file, cameraControls, timelinePane, startFromFrame, endFrame);
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
		scene = new Scene(root);//, 1024, 768);
		scene.setFill(Color.GRAY);
		stage.setTitle("SKVP 3D Player (SKeleton Video Player)");
		stage.setScene(scene);		
	}

	public static void main(String[] args) {
		launch(args);
	}

}
