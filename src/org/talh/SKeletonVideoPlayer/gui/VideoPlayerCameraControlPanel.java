package org.talh.SKeletonVideoPlayer.gui;

import java.util.HashSet;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;

public class VideoPlayerCameraControlPanel extends GridPane implements VideoPlayerCameraControlInterface {

	private Spinner<Double> spinnerCameraX;
	private Spinner<Double> spinnerCameraZ;
	private Spinner<Double> spinnerCameraY;
	private Spinner<Double> spinnerCameraDestinationX;
	private Spinner<Double> spinnerCameraDestinationY;
	private Spinner<Double> spinnerCameraDestinationZ;
	private Spinner<Double> spinnerCameraSceneRotation;
	HashSet<VideoPlayerCameraControlListener> listeners = new HashSet<VideoPlayerCameraControlListener>();

	public VideoPlayerCameraControlPanel() {
		spinnerCameraX = new Spinner<Double>(-999999999.0, 999999999.0, 0.0);
		spinnerCameraY = new Spinner<Double>(-999999999.0, 999999999.0, 0.0);
		spinnerCameraZ = new Spinner<Double>(-999999999.0, 999999999.0, 0.0);
		spinnerCameraDestinationX = new Spinner<Double>(-999999999.0, 999999999.0, 0);
		spinnerCameraDestinationY = new Spinner<Double>(-999999999.0, 999999999.0, 0);
		spinnerCameraDestinationZ = new Spinner<Double>(-999999999.0, 999999999.0, 0);
		spinnerCameraSceneRotation = new Spinner<Double>(-999999999.0, 999999999.0, 0);
		
		
		this.add(new Label("Location X:"), 1, 1);
		this.add(spinnerCameraX, 2, 1);
		this.add(new Label("    "), 3, 1);
		this.add(new Label("Location Y:"), 4, 1);
		this.add(spinnerCameraY, 5, 1);
		this.add(new Label("    "), 6, 1);
		this.add(new Label("Location Z:"), 7, 1);
		this.add(spinnerCameraZ, 8, 1);
		this.add(new Label("Destination X:"), 1, 2);
		this.add(spinnerCameraDestinationX, 2, 2);
		this.add(new Label("Destination Y:"), 4, 2);
		this.add(spinnerCameraDestinationY, 5, 2);
		this.add(new Label("Destination Z:"), 7, 2);
		this.add(spinnerCameraDestinationZ, 8, 2);
		this.add(new Label("Scene Rotation:"), 1, 3);
		this.add(spinnerCameraSceneRotation, 2, 3);
		setupEvents();
	}
	
	private void notifyAllListenersOnValueChangeEvent(ControlType controlType, double value) {
		for (VideoPlayerCameraControlListener listener : listeners) {
			Thread t = new Thread() {
				public void run() {
					listener.controllerValueChanged(controlType, value);
				}
			};
			t.start();
		}
	}
	
	private void setupValueChangeEventOnspinner(Spinner<Double> spinner, ControlType controlType) {
		spinner.valueProperty().addListener(new ChangeListener<Double>() {
			@Override
			public void changed(ObservableValue<? extends Double> arg0, Double arg1, Double arg2) {
				notifyAllListenersOnValueChangeEvent(controlType, spinner.getValue());
			}
		});
	}
	
	private void setupEvents() {
		setupValueChangeEventOnspinner(spinnerCameraX, ControlType.LOCATION_X);
		setupValueChangeEventOnspinner(spinnerCameraY, ControlType.LOCATION_Y);
		setupValueChangeEventOnspinner(spinnerCameraZ, ControlType.LOCATION_Z);
		setupValueChangeEventOnspinner(spinnerCameraDestinationX, ControlType.DESTINATION_X);
		setupValueChangeEventOnspinner(spinnerCameraDestinationY, ControlType.DESTINATION_Y);
		setupValueChangeEventOnspinner(spinnerCameraDestinationZ, ControlType.DESTINATION_Z);
		setupValueChangeEventOnspinner(spinnerCameraSceneRotation, ControlType.SCENE_ROTATION);
	}

	@Override
	public void setControllerEnabled(ControlType controlType, boolean ans) {
		boolean disable = !ans;
		switch (controlType) {
			case DESTINATION:
				setControllerEnabled(ControlType.DESTINATION_X, ans);
				setControllerEnabled(ControlType.DESTINATION_Y, ans);
				setControllerEnabled(ControlType.DESTINATION_Z, ans);
				break;
			case DESTINATION_X:
				spinnerCameraDestinationX.setDisable(disable);
				break;
			case DESTINATION_Y:
				spinnerCameraDestinationY.setDisable(disable);
				break;
			case DESTINATION_Z:
				spinnerCameraDestinationZ.setDisable(disable);
				break;
			case LOCATION:
				setControllerEnabled(ControlType.LOCATION_X, ans);
				setControllerEnabled(ControlType.LOCATION_Y, ans);
				setControllerEnabled(ControlType.LOCATION_Z, ans);
				break;
			case LOCATION_X:
				spinnerCameraX.setDisable(disable);
				break;
			case LOCATION_Y:
				spinnerCameraY.setDisable(disable);
				break;
			case LOCATION_Z:
				spinnerCameraZ.setDisable(disable);
				break;
			case SCENE_ROTATION:
				spinnerCameraSceneRotation.setDisable(disable);
				break;
			case ZOOM:
				setControllerEnabled(ControlType.ZOOM_IN, ans);
				setControllerEnabled(ControlType.ZOOM_OUT, ans);
				setControllerEnabled(ControlType.ZOOM_SCALE, ans);
				setControllerEnabled(ControlType.ZOOM_STEP, ans);
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

	@Override
	public void setControllersEnabled(ControlType[] controlTypes, boolean ans) {
		for (ControlType controlType : controlTypes) {
			setControllerEnabled(controlType, ans);
		}
	}

	@Override
	public void setAllControllersEnabled(boolean ans) {
		for (ControlType controlType : ControlType.values()) {
			setControllerEnabled(controlType, ans);
		}
	}

	@Override
	public void setControllerEditable(ControlType controlType, boolean ans) {
		switch (controlType) {
			case DESTINATION:
				setControllerEditable(ControlType.DESTINATION_X, ans);
				setControllerEditable(ControlType.DESTINATION_Y, ans);
				setControllerEditable(ControlType.DESTINATION_Z, ans);
				break;
			case DESTINATION_X:
				spinnerCameraDestinationX.setEditable(ans);
				break;
			case DESTINATION_Y:
				spinnerCameraDestinationY.setEditable(ans);
				break;
			case DESTINATION_Z:
				spinnerCameraDestinationZ.setEditable(ans);
				break;
			case LOCATION:
				setControllerEditable(ControlType.LOCATION_X, ans);
				setControllerEditable(ControlType.LOCATION_Y, ans);
				setControllerEditable(ControlType.LOCATION_Z, ans);
				break;
			case LOCATION_X:
				spinnerCameraX.setEditable(ans);
				break;
			case LOCATION_Y:
				spinnerCameraY.setEditable(ans);
				break;
			case LOCATION_Z:
				spinnerCameraZ.setEditable(ans);
				break;
			case SCENE_ROTATION:
				spinnerCameraSceneRotation.setEditable(ans);
				break;
			case ZOOM:
				setControllerEditable(ControlType.ZOOM_IN, ans);
				setControllerEditable(ControlType.ZOOM_OUT, ans);
				setControllerEditable(ControlType.ZOOM_SCALE, ans);
				setControllerEditable(ControlType.ZOOM_STEP, ans);
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

	@Override
	public void setControllersEditable(ControlType[] controlTypes, boolean ans) {
		for (ControlType controlType : controlTypes) {
			setControllerEditable(controlType, ans);
		}
		
	}

	@Override
	public void setAllControllersEditable(boolean ans) {
		for (ControlType controlType : ControlType.values()) {
			setControllerEditable(controlType, ans);
		}
	}
	
	@Override
	public void setControllerValue(ControlType controlType, double value) {
		switch (controlType) {
		case DESTINATION:
			break;
		case DESTINATION_X:
			spinnerCameraDestinationX.getValueFactory().setValue(value);
			break;
		case DESTINATION_Y:
			spinnerCameraDestinationY.getValueFactory().setValue(value);
			break;
		case DESTINATION_Z:
			spinnerCameraDestinationZ.getValueFactory().setValue(value);
			break;
		case LOCATION:
			break;
		case LOCATION_X:
			spinnerCameraX.getValueFactory().setValue(value);
			break;
		case LOCATION_Y:
			spinnerCameraY.getValueFactory().setValue(value);
			break;
		case LOCATION_Z:
			spinnerCameraZ.getValueFactory().setValue(value);
			break;
		case SCENE_ROTATION:
			spinnerCameraSceneRotation.getValueFactory().setValue(value);
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

	@Override
	public void addVideoPlayerCameraControlListener(VideoPlayerCameraControlListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeVideoPlayerCameraControlListener(VideoPlayerCameraControlListener listener) {
		listeners.remove(listener);
	}

	@Override
	public Double getControllerValue(ControlType controlType) {
		switch (controlType) {
			case DESTINATION:
				break;
			case DESTINATION_X:
				return spinnerCameraDestinationX.getValue();
			case DESTINATION_Y:
				return spinnerCameraDestinationY.getValue();
			case DESTINATION_Z:
				return spinnerCameraDestinationZ.getValue();
			case LOCATION:
				break;
			case LOCATION_X:
				return spinnerCameraX.getValue();
			case LOCATION_Y:
				return spinnerCameraY.getValue();
			case LOCATION_Z:
				return spinnerCameraZ.getValue();
			case SCENE_ROTATION:
				return spinnerCameraSceneRotation.getValue();
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
		return null;
	}

}
