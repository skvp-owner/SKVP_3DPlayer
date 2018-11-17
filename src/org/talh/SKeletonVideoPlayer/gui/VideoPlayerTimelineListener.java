package org.talh.SKeletonVideoPlayer.gui;

public interface VideoPlayerTimelineListener {
	public void timeJumpRequest(long requestedFrame, Long endFrame);
	public void userModificationRequest();
	void playFpsChangeRequest(double fps);
}
