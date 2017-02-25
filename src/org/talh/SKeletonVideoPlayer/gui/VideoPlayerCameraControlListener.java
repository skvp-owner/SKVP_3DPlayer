package org.talh.SKeletonVideoPlayer.gui;

import org.talh.SKeletonVideoPlayer.gui.VideoPlayerCameraControlInterface.ControlType;

public interface VideoPlayerCameraControlListener {
	public void controllerValueChanged(ControlType controlType, double value);
}
