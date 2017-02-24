package org.talh.SKeletonVideoPlayer.gui;


public interface VideoPlayerButtonsInterface {
	public enum ButtonType {
		PLAY,
		STOP,
		PAUSE,
		OPEN,
		FF,
		RW,
		NEXT,
		PREVIOUS
	}
	
	public void setAllButtonsEnabled(boolean ans);
	public void setButtonEnabled(ButtonType buttonType, boolean ans);
	public void setButtonsEnabled(ButtonType[] buttonTypes, boolean ans);
	public void setAllButtonsSelected(boolean ans);
	public void setButtonSelected(ButtonType buttonType, boolean ans);
	public void setButtonsSelected(ButtonType[] buttonTypes, boolean ans);
}
