package org.talh.SKeletonVideoPlayer.gui;

public interface VideoPlayerTimelineListener {
	public void timeJumpRequest(long requestedFrame);
	public void userModificationRequest();
}
