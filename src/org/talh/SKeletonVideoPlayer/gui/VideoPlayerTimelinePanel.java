package org.talh.SKeletonVideoPlayer.gui;

import java.util.HashSet;

import org.talh.SKeletonVideoPlayer.player.Defs;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class VideoPlayerTimelinePanel extends HBox implements VideoPlayerTimelineInterface {

	private AppMainContainer mainContainer;
	private Label lblCurrentTime;
	private Label lblCurrentFrame;
	private TextField tfJumpToFrame;
	private Button btnJumpToFrame;
	private Label lblTotalTime;
	private Label lblTotalFrames;
	private Slider slider;
	private double fps;
	private boolean fpsIsDefined = false;
	private Long videoLengthInFrames = null;
	private long currentFrame = 0;
	private HashSet<VideoPlayerTimelineListener> listeners = new HashSet<VideoPlayerTimelineListener>();
	private boolean sliderChangeEventOff = true;
	private TextField tfFPS;
	private Button btnSetFps;
	private TextField tfStartFrame;
	private TextField tfEndFrame;
	private Button btnPlaySequence;
	private Button btnCutSequence;
	private TextField tfClipName;
	
	
	public VideoPlayerTimelinePanel(AppMainContainer mainContainer) {
		super(Defs.BUTTONS_PANEL_BETWEEN_SPACING);
		this.mainContainer = mainContainer;
		VBox currTimeBox = new VBox();
		lblCurrentTime = new Label("00:00:00");
		lblCurrentTime.setTextAlignment(TextAlignment.CENTER);
		lblCurrentFrame = new Label("0");
		lblCurrentFrame.setTextAlignment(TextAlignment.CENTER);
		currTimeBox.setAlignment(Pos.BASELINE_CENTER);
		currTimeBox.getChildren().addAll(lblCurrentTime, lblCurrentFrame);
		VBox totalTimeBox = new VBox();
		lblTotalTime = new Label("00:13:05");
		lblTotalTime.setTextAlignment(TextAlignment.CENTER);
		lblTotalFrames = new Label("123744");
		lblTotalFrames.setTextAlignment(TextAlignment.CENTER);
		totalTimeBox.getChildren().addAll(lblTotalTime, lblTotalFrames);
		totalTimeBox.setAlignment(Pos.BASELINE_CENTER);
		slider = new Slider();
		HBox sequenceBox = new HBox();
		VBox sequenceFieldsBox = new VBox();
		VBox sequenceButtonsBox = new VBox();
		sequenceBox.getChildren().addAll(sequenceFieldsBox, sequenceButtonsBox);
		HBox sequenceStartFrameBox = new HBox();
		HBox sequenceEndFrameBox = new HBox();
		HBox sequenceClipNameBox = new HBox();
		sequenceFieldsBox.getChildren().addAll(sequenceStartFrameBox, sequenceEndFrameBox, sequenceClipNameBox);
		Label lblStartFrame = new Label("Start:");
		tfStartFrame = new TextField("");
		sequenceStartFrameBox.getChildren().addAll(lblStartFrame, tfStartFrame);
		Label lblEndFrame = new Label("End:");
		tfEndFrame = new TextField("");
		sequenceEndFrameBox.getChildren().addAll(lblEndFrame, tfEndFrame);
		Label lblClipName = new Label("Clip Name:");
		tfClipName = new TextField("");
		sequenceClipNameBox.getChildren().addAll(lblClipName, tfClipName);
		btnPlaySequence = new Button("Play");
		btnCutSequence = new Button("Cut");
		sequenceButtonsBox.getChildren().addAll(btnPlaySequence, btnCutSequence);
		/*
		HBox jumpToFrameBox = new HBox();
		Label lblJumpToFrame = new Label("Frame:");
		tfJumpToFrame = new TextField("");
		tfJumpToFrame.setPrefColumnCount(5);
		btnJumpToFrame = new Button("Jump");
		jumpToFrameBox.getChildren().addAll(lblJumpToFrame, tfJumpToFrame, btnJumpToFrame);
		*/
		HBox changeFpsBox = new HBox();
		Label lblFps = new Label("FPS:");
		tfFPS = new TextField("");
		tfFPS.setPrefColumnCount(5);
		btnSetFps = new Button("Set");
		changeFpsBox.getChildren().addAll(lblFps, tfFPS, btnSetFps);
		VBox jumpersBox = new VBox();
		jumpersBox.getChildren().addAll(sequenceBox, changeFpsBox);
		this.getChildren().addAll(currTimeBox, slider, totalTimeBox, jumpersBox);
		HBox.setHgrow(slider, Priority.ALWAYS);
		updateAllPossibleControllers();
		setupEvents();
	}
	
	private void updateAllListenersOnJumpRequest(long destFrame, Long endFrame) {
		for (VideoPlayerTimelineListener listener : listeners) {
		//	Thread t = new Thread() {
		//		public void run() {
					listener.timeJumpRequest(destFrame, endFrame);
		//		}
		//	};
		//	t.start();
		}
	}
	
	private void updateAllListenersOnUserModificationRequest() {
		for (VideoPlayerTimelineListener listener : listeners) {
		//	Thread t = new Thread() {
			//	public void run() {
					listener.userModificationRequest();
			//	}
			//};
			//t.start();
		}
	}
	
	private void updateAllListenersOnFpsChangeRequest(double fps) {
		for (VideoPlayerTimelineListener listener : listeners) {
			//	Thread t = new Thread() {
			//		public void run() {
						listener.playFpsChangeRequest(fps);
			//		}
			//	};
			//	t.start();
		}
	}
	
	private void showErrorDialog(String title, String errorHeader, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(errorHeader);
		alert.setContentText(content);

		alert.showAndWait();
	}
	
	private void setupEvents() {
		slider.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				long newFrame = (long)slider.getValue();
				sliderChangeEventOff = true;
				updateAllListenersOnJumpRequest(newFrame, null);
			}			
		});
		slider.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				updateAllListenersOnUserModificationRequest();
				sliderChangeEventOff = false;
			}			
		});
		slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                if (sliderChangeEventOff) {
                	return;
                }
                // The purpose of this event is reflect the slider state in the current frame label
                //   so the user can see the index of the frame he is trying to pick 
            	setCurrentFrame(new_val.longValue());
            }
        });
		btnPlaySequence.setOnAction(e -> {
			long startFrameNum;
			long endFrameNum;
			try {
				startFrameNum = Long.parseLong(tfStartFrame.getText());
				endFrameNum = Long.parseLong(tfEndFrame.getText());
			} catch (NumberFormatException e1) {
				showErrorDialog("Input error", "", "start and end frames must be integers");
				return;
			}
			
			sliderChangeEventOff = true;
			updateAllListenersOnUserModificationRequest();
			//slider.setValue(frameNum);
			updateAllListenersOnJumpRequest(startFrameNum, endFrameNum);
			
		});
		btnCutSequence.setOnAction(e -> {
			if (tfClipName.getText().equals("")) {
				showErrorDialog("Input error", "", "clip name must be defined");
				return;
			}
			long startFrameNum;
			long endFrameNum;
			try {
				startFrameNum = Long.parseLong(tfStartFrame.getText());
				endFrameNum = Long.parseLong(tfEndFrame.getText());
			} catch (NumberFormatException e1) {
				showErrorDialog("Input error", "", "start and end frames must be integers");
				return;
			}
			String inputFilePath = mainContainer.getPlayedFile().getAbsolutePath();
			String cmd = "python " + 
						"C:\\projects\\thesis_algorithms\\scripts\\cut_skvp.py " +
						inputFilePath + " " +
						tfClipName.getText() + " " +
						Long.toString(startFrameNum) + " " +
						Long.toString(endFrameNum) + " " + 
						"C:\\projects\\thesis_raw_videos_indexed\\cuts";
			try {
				Process p = Runtime.getRuntime().exec(cmd);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		});
		/*
		btnJumpToFrame.setOnAction(e -> {
			int frameNum;
			try {
				frameNum = Integer.parseInt(tfJumpToFrame.getText());
			} catch (NumberFormatException e1) {
				return;
			}
			
			sliderChangeEventOff = true;
			updateAllListenersOnUserModificationRequest();
			//slider.setValue(frameNum);
			updateAllListenersOnJumpRequest(frameNum);
			
		});*/
		
		btnSetFps.setOnAction(e -> {
			double fpsVal;
			try {
				fpsVal = Double.parseDouble(tfFPS.getText()) ;
			} catch (NumberFormatException e1) {
				return;
			}
			updateAllListenersOnFpsChangeRequest(fpsVal);
		});
		
	}

	

	@Override
	public void setControllerEnabled(TimeControllerType timeControllerType, boolean ans) {
		boolean disable = !ans;
		switch (timeControllerType) {
			case FRAME:
				setControllerEnabled(TimeControllerType.FRAME_CURRENT, ans);
				setControllerEnabled(TimeControllerType.FRAME_TOTAL, ans);
				break;
			case FRAME_CURRENT:
				lblCurrentFrame.setDisable(disable);
				break;
			case FRAME_TOTAL:
				lblTotalFrames.setDisable(disable);
				break;
			case SLIDER:
				slider.setDisable(disable);
				break;
			case TIME:
				setControllerEnabled(TimeControllerType.TIME_CURRENT, ans);
				setControllerEnabled(TimeControllerType.TIME_TOTAL, ans);
				break;
			case TIME_CURRENT:
				lblCurrentTime.setDisable(disable);
				break;
			case TIME_TOTAL:
				lblTotalTime.setDisable(disable);
				break;
			case TIME_FRAME:
				setControllerEnabled(TimeControllerType.TIME_FRAME_CURRENT, ans);
				setControllerEnabled(TimeControllerType.TIME_FRAME_TOTAL, ans);
				break;
			case TIME_FRAME_CURRENT:
				setControllerEnabled(TimeControllerType.TIME_CURRENT, ans);
				setControllerEnabled(TimeControllerType.FRAME_CURRENT, ans);
				break;
			case TIME_FRAME_TOTAL:
				setControllerEnabled(TimeControllerType.TIME_TOTAL, ans);
				setControllerEnabled(TimeControllerType.FRAME_TOTAL, ans);
				break;
			default:
				break;		
		}		
	}

	@Override
	public void setControllersEnabled(TimeControllerType[] timeControllerTypes, boolean ans) {
		for (TimeControllerType timeControllerType : timeControllerTypes) {
			setControllerEnabled(timeControllerType, ans);
		}
	}

	@Override
	public void setAllControllersEnabled(boolean ans) {
		for (TimeControllerType timeControllerType : TimeControllerType.values()) {
			setControllerEnabled(timeControllerType, ans);
		}
	}

	@Override
	public void setControllerVisible(TimeControllerType timeControllerType, boolean ans) {
		switch (timeControllerType) {
			case FRAME:
				setControllerVisible(TimeControllerType.FRAME_CURRENT, ans);
				setControllerVisible(TimeControllerType.FRAME_TOTAL, ans);
				break;
			case FRAME_CURRENT:
				lblCurrentFrame.setVisible(ans);
				break;
			case FRAME_TOTAL:
				lblTotalFrames.setVisible(ans);
				break;
			case SLIDER:
				slider.setVisible(ans);
				break;
			case TIME:
				setControllerVisible(TimeControllerType.TIME_CURRENT, ans);
				setControllerVisible(TimeControllerType.TIME_TOTAL, ans);
				break;
			case TIME_CURRENT:
				lblCurrentTime.setVisible(ans);
				break;
			case TIME_TOTAL:
				lblTotalTime.setVisible(ans);
				break;
			case TIME_FRAME:
				setControllerVisible(TimeControllerType.TIME_FRAME_CURRENT, ans);
				setControllerVisible(TimeControllerType.TIME_FRAME_TOTAL, ans);
				break;
			case TIME_FRAME_CURRENT:
				setControllerVisible(TimeControllerType.TIME_CURRENT, ans);
				setControllerVisible(TimeControllerType.FRAME_CURRENT, ans);
				break;
			case TIME_FRAME_TOTAL:
				setControllerVisible(TimeControllerType.TIME_TOTAL, ans);
				setControllerVisible(TimeControllerType.FRAME_TOTAL, ans);
				break;
			default:
				break;		
		}
		
	}

	@Override
	public void setControllersVisible(TimeControllerType[] timeControllerTypes, boolean ans) {
		for (TimeControllerType timeControllerType : timeControllerTypes) {
			setControllerVisible(timeControllerType, ans);
		}
	}

	@Override
	public void setAllControllersVisible(boolean ans) {
		for (TimeControllerType timeControllerType : TimeControllerType.values()) {
			setControllerVisible(timeControllerType, ans);
		}
	}
	
	private void setSliderDisabled() {
		slider.setMin(0);
		slider.setMax(1);
		slider.setValue(0);
		setControllerEnabled(TimeControllerType.SLIDER, false);
	}
	
	private void setSliderEnabled(long max, long curr) {
		setControllerEnabled(TimeControllerType.SLIDER, true);
		slider.setMin(0);
		slider.setMax(max);
		slider.setValue(curr);		
	}
	
	private void updateAllPossibleControllers() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				boolean canUpdateSlider = (videoLengthInFrames != null && fpsIsDefined);
				boolean canUpdateCurrTime = fpsIsDefined;
				boolean canUpdateTotalFrames = (videoLengthInFrames != null);
				boolean canUpdateTotalTime = (videoLengthInFrames != null && fpsIsDefined);
				if (canUpdateSlider) {
					setSliderEnabled(videoLengthInFrames, currentFrame);
				} else {
					setSliderDisabled();
				}
				if (canUpdateCurrTime) {
					long currNumTotalSeconds = (long) (currentFrame / fps);
					String currTimeAsString = secondsToTimeString(currNumTotalSeconds);
					lblCurrentTime.setText(currTimeAsString);
				} else {
					lblCurrentTime.setText("N/A");
				}
				if (canUpdateTotalFrames) {
					lblTotalFrames.setText(Long.toString(videoLengthInFrames));
				} else {
					lblTotalFrames.setText("N/A");
				}
				if (canUpdateTotalTime) {
					long numTotalSeconds = (long) (videoLengthInFrames / fps);
					String totalTimeAsString = secondsToTimeString(numTotalSeconds);
					lblTotalTime.setText(totalTimeAsString);
				} else {
					lblTotalTime.setText("N/A");
				}
				lblCurrentFrame.setText(Long.toString(currentFrame));
			}
		});
		
			}

	@Override
	public void setFps(double fps) {
		// FPS must be positive and non-zero
		this.fps = fps;
		this.fpsIsDefined = true;
		updateAllPossibleControllers();
	}

	@Override
	public void setVideoLengthInFrames(Long length) {
		this.videoLengthInFrames  = length;
		updateAllPossibleControllers();
	}

	@Override
	public void setCurrentFrame(long frame) {
		this.currentFrame  = frame;
		updateAllPossibleControllers();
	}

	@Override
	public void addVideoPlayerTimelineListener(VideoPlayerTimelineListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeVideoPlayerTimelineListener(VideoPlayerTimelineListener listener) {
		listeners.remove(listener);
	}
	
	private String secondsToTimeString(long currNumTotalSeconds) {
		long numTotalHours = currNumTotalSeconds / 3600;
		long numRemainingMinutes = (currNumTotalSeconds - (numTotalHours * 3600)) / 60;
		long numRemainingSeconds = currNumTotalSeconds - (numTotalHours * 3600) - (numRemainingMinutes * 60);
		String hoursString = Long.toString(numTotalHours);
		String minutesString = Long.toString(numRemainingMinutes);
		String secondsString = Long.toString(numRemainingSeconds);		
		if (hoursString.length() == 1) {
			hoursString = "0" + hoursString;
		}
		if (minutesString.length() == 1) {
			minutesString = "0" + minutesString;
		}
		if (secondsString.length() == 1) {
			secondsString = "0" + secondsString;
		}
		
		return hoursString + ":" + minutesString + ":" + secondsString;
	}

}
