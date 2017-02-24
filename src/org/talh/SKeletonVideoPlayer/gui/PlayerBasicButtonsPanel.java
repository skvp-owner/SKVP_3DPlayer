package org.talh.SKeletonVideoPlayer.gui;

import java.util.HashSet;

import org.talh.SKeletonVideoPlayer.player.Defs;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class PlayerBasicButtonsPanel extends HBox implements VideoPlayerButtonsInterface{
	
	private Button buttonStop;
	private Button buttonPause;
	private Button buttonPlay;
	private HashSet<VideoPlayerButtonsListener> listeners = new HashSet<VideoPlayerButtonsListener>(); 

	public PlayerBasicButtonsPanel() {
		super(Defs.BUTTONS_PANEL_BETWEEN_SPACING);
		Image imageBtnStop = new Image(getClass().getResourceAsStream("resources/icons/stop_button.png"));
		Image imageBtnPause = new Image(getClass().getResourceAsStream("resources/icons/pause_button.png"));
		Image imageBtnPlay = new Image(getClass().getResourceAsStream("resources/icons/play_button.png"));
		buttonStop = new Button();
		buttonStop.setGraphic(new ImageView(imageBtnStop));
		buttonPause = new Button();
		buttonPause.setGraphic(new ImageView(imageBtnPause));
		buttonPlay = new Button();
		buttonPlay.setGraphic(new ImageView(imageBtnPlay));
		this.getChildren().add(buttonPlay);
		this.getChildren().add(buttonPause);
		this.getChildren().add(buttonStop);
		setAllButtonsSelected(false);
		setAllButtonsEnabled(false);
		setupEvents();
	}
	
	private void notifyAllListenersOnSelectionEvent(ButtonType buttonType) {
		for (VideoPlayerButtonsListener listener : listeners) {
			Thread t = new Thread() {
				public void run() {
					listener.buttonSelected(buttonType);
				}
			};
			t.start();
		}
	}
	
	private void setupEvents() {
		buttonPlay.setOnAction((ActionEvent event) -> {
			notifyAllListenersOnSelectionEvent(ButtonType.PLAY);
		});
		buttonPause.setOnAction((ActionEvent event) -> {
			notifyAllListenersOnSelectionEvent(ButtonType.PAUSE);
		});
		buttonStop.setOnAction((ActionEvent event) -> {
			notifyAllListenersOnSelectionEvent(ButtonType.STOP);
		});
		
	}

	@Override
	public void setAllButtonsEnabled(boolean ans) {
		for (ButtonType buttonType : ButtonType.values()) {
			setButtonEnabled(buttonType, ans);
		}
	}

	@Override
	public void setButtonEnabled(ButtonType buttonType, boolean ans) {
		switch (buttonType) {
			case PAUSE:
				buttonPause.setDisable(! ans);
				break;
			case PLAY:
				buttonPlay.setDisable(! ans);
				break;
			case STOP:
				buttonStop.setDisable(! ans);
				break;
			default:
				break;		
		}
	}

	@Override
	public void setButtonsEnabled(ButtonType[] buttonTypes, boolean ans) {
		for (ButtonType buttonType : buttonTypes) {
			setButtonEnabled(buttonType, ans);
		}
	}

	@Override
	public void setAllButtonsSelected(boolean ans) {
		for (ButtonType buttonType : ButtonType.values()) {
			setButtonSelected(buttonType, ans);
		}
	}

	@Override
	public void setButtonSelected(ButtonType buttonType, boolean ans) {
		// Currently not implemented
		
	}

	@Override
	public void setButtonsSelected(ButtonType[] buttonTypes, boolean ans) {
		for (ButtonType buttonType : buttonTypes) {
			setButtonSelected(buttonType, ans);
		}
	}
	
	public void addVideoPlayerButtonsListener(VideoPlayerButtonsListener listener) {
		listeners.add(listener);
	}
	
	public void removeVideoPlayerButtonsListener(VideoPlayerButtonsListener listener) {
		listeners.remove(listener);
	}

}
