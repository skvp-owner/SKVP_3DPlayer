package org.talh.SKeletonVideoPlayer.gui;

public interface VideoPlayerTimelineInterface {
	enum TimeControllerType {
		SLIDER,
		TIME_FRAME,
		TIME_FRAME_CURRENT,
		TIME_FRAME_TOTAL,
		TIME,
		FRAME,
		TIME_CURRENT,
		TIME_TOTAL,
		FRAME_CURRENT,
		FRAME_TOTAL
	}
	
	public void setControllerEnabled(TimeControllerType timeControllerType, boolean ans);
	public void setControllersEnabled(TimeControllerType[] timeControllerTypes, boolean ans);
	public void setAllControllersEnabled(boolean ans);
	
	public void setControllerVisible(TimeControllerType timeControllerType, boolean ans);
	public void setControllersVisible(TimeControllerType[] timeControllerTypes, boolean ans);
	public void setAllControllersVisible(boolean ans);
	
	public void setFps(double fps);
	public void setVideoLengthInFrames(Long length);
	public void setCurrentFrame(long frame);
	public void addVideoPlayerTimelineListener(VideoPlayerTimelineListener listener);
	public void removeVideoPlayerTimelineListener(VideoPlayerTimelineListener listener);
}
