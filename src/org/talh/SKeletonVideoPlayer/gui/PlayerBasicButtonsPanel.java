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
	private Button buttonOpen;
	private HashSet<VideoPlayerButtonsListener> listeners = new HashSet<VideoPlayerButtonsListener>(); 

	public PlayerBasicButtonsPanel() {
		super(Defs.BUTTONS_PANEL_BETWEEN_SPACING);
		Image imageBtnStop = new Image(getClass().getResourceAsStream("resources/icons/stop_button.png"));
		Image imageBtnPause = new Image(getClass().getResourceAsStream("resources/icons/pause_button.png"));
		Image imageBtnPlay = new Image(getClass().getResourceAsStream("resources/icons/play_button.png"));
		Image imageBtnOpen = new Image(getClass().getResourceAsStream("resources/icons/open_button.png"));
		buttonStop = new Button();
		buttonStop.setGraphic(new ImageView(imageBtnStop));
		buttonPause = new Button();
		buttonPause.setGraphic(new ImageView(imageBtnPause));
		buttonPlay = new Button();
		buttonPlay.setGraphic(new ImageView(imageBtnPlay));
		buttonOpen = new Button();
		buttonOpen.setGraphic(new ImageView(imageBtnOpen));
		this.getChildren().add(buttonPlay);
		this.getChildren().add(buttonPause);
		this.getChildren().add(buttonStop);
		this.getChildren().add(buttonOpen);
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
		buttonOpen.setOnAction((ActionEvent event) -> {
			notifyAllListenersOnSelectionEvent(ButtonType.OPEN);
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
		boolean disable = !ans;
		switch (buttonType) {
			case PAUSE:
				buttonPause.setDisable(disable);
				break;
			case PLAY:
				buttonPlay.setDisable(disable);
				break;
			case STOP:
				buttonStop.setDisable(disable);
				break;
			case OPEN:
				buttonOpen.setDisable(disable);
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
	
	@Override
	public void addVideoPlayerButtonsListener(VideoPlayerButtonsListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void removeVideoPlayerButtonsListener(VideoPlayerButtonsListener listener) {
		listeners.remove(listener);
	}

}
