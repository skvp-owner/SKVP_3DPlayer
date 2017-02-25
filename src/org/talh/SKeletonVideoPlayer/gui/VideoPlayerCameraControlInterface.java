package org.talh.SKeletonVideoPlayer.gui;

public interface VideoPlayerCameraControlInterface {
	public enum ControlType {
		LOCATION,
		DESTINATION,
		SCENE_ROTATION,
		ZOOM,
		LOCATION_X,
		LOCATION_Y,
		LOCATION_Z,
		DESTINATION_X,
		DESTINATION_Y,
		DESTINATION_Z,
		ZOOM_IN,
		ZOOM_OUT,
		ZOOM_SCALE,
		ZOOM_STEP
	}
	
	public void setControllerEnabled(ControlType controlType, boolean ans);
	public void setControllersEnabled(ControlType[] controlTypes, boolean ans);
	public void setAllControllersEnabled(boolean ans);
	public void setControllerEditable(ControlType controlType, boolean ans);
	public void setControllersEditable(ControlType[] controlTypes, boolean ans);
	public void setAllControllersEditable(boolean ans);
	public void setControllerValue(ControlType controlType, double value);
	public void addVideoPlayerCameraControlListener(VideoPlayerCameraControlListener listener);
	public void removeVideoPlayerCameraControlListener(VideoPlayerCameraControlListener listener);
	public Double getControllerValue(ControlType controlType);
	
}
