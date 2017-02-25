package org.talh.SKeletonVideoPlayer.player;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Semaphore;

import org.talh.SKeletonVideoPlayer.Coordinate3D;
import org.talh.SKeletonVideoPlayer.SKVPIllegalValueException;
import org.talh.SKeletonVideoPlayer.SKVPNonInitializedReaderException;
import org.talh.SKeletonVideoPlayer.SKVPReader;
import org.talh.SKeletonVideoPlayer.SKVPSyntaxErrorException;
import org.talh.SKeletonVideoPlayer.graph.Rendered3DGraph;
import org.talh.SKeletonVideoPlayer.gui.VideoPlayerCameraControlInterface;
import org.talh.SKeletonVideoPlayer.gui.VideoPlayerCameraControlInterface.ControlType;

import javafx.application.Platform;


public class PlayerBackend {
	
	private PlayerBuffer playerBuffer = null;
	private Player3DRenderer renderer;
	private double fps;
	private Semaphore playSemaphore = new Semaphore(1, true);
	private PlayerState playerState = PlayerState.STOPPED;
	Coordinate3D cameraLocation;
	Coordinate3D cameraDestination;
	double cameraSceneRotation;
		
	
	public void clear() {
		if (playerBuffer == null) {
			return;
		}
		playerBuffer.dismissBuffer();
		playerBuffer = null;
	}
	
	public void loadFile(File file, VideoPlayerCameraControlInterface cameraControls) throws SKVPSyntaxErrorException, IOException, SKVPIllegalValueException {
		clear();
		SKVPReader reader = new SKVPReader(file);
		try {
			fps = reader.getFps();
			cameraLocation = reader.getCameraLocation();
			cameraDestination = reader.getCameraDestination();
			cameraSceneRotation = reader.getCameraSceneRotation();
		} catch (SKVPNonInitializedReaderException e) {
			// Don't care - it is initialized (see c'tor)
			e.printStackTrace();
		}
		cameraControls.setControllerValue(ControlType.LOCATION_X, cameraLocation.getX());
		cameraControls.setControllerValue(ControlType.LOCATION_Y, cameraLocation.getY());
		cameraControls.setControllerValue(ControlType.LOCATION_Z, cameraLocation.getZ());
		cameraControls.setControllerValue(ControlType.DESTINATION_X, cameraDestination.getX());
		cameraControls.setControllerValue(ControlType.DESTINATION_Y, cameraDestination.getY());
		cameraControls.setControllerValue(ControlType.DESTINATION_Z, cameraDestination.getZ());
		playerBuffer = new PlayerBuffer(reader);		
	}
	
	public void setPlayerRenderer(Player3DRenderer renderer) {
		this.renderer = renderer;
	}
	
	public synchronized void play(Double fps) {
		switch (playerState) {
			case STOPPED:
				playerState = PlayerState.PLAYING;
				break;
			case PAUSED:
				resume();
				return;
			case PLAYING:
				return;
			default:
				return;
		}
		//fps = 1.0;
		final double fpsVal = (fps == null) ? this.fps : fps.doubleValue(); 
		Thread playThread = new Thread() {
			public void run() {
				Rendered3DGraph graph = null;
				while (playerState != PlayerState.STOPPED && 
								(graph = playerBuffer.getNextFrameGraph()) != null) {
					try {
						playSemaphore.acquire();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Platform.runLater(new RendererThread(renderer, graph));
					//final Rendered3DGraph finalGraph = graph;
					/*Platform.runLater(new Runnable() {
						@Override
						public void run() {
							renderer.paintSkeletonFrame(finalGraph);
						}
						
					});*/
					try {
						Thread.sleep((int)(1000.0 / fpsVal));
						//System.out.println("fps is: " + fpsVal);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					playSemaphore.release();
				}
				playerState = PlayerState.STOPPED;
			}
		};
		playThread.start();
	}
	
	public synchronized void pause() {
		if (playerState != PlayerState.PLAYING) {
			return;
		}
		try {
			playSemaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		playerState = PlayerState.PAUSED;
	}
	
	public synchronized void resume() {
		if (playerState != PlayerState.PAUSED) {
			return;
		}
		playSemaphore.release();
		playerState = PlayerState.PLAYING;
	}
	
	public synchronized void stop() {
		if (playerState == PlayerState.STOPPED) {
			return;
		}
		playerState = PlayerState.STOPPED;
	}
	
}
