package org.talh.SKeletonVideoPlayer.player;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

import org.talh.SKeletonVideoPlayer.SKVPNonInitializedReaderException;
import org.talh.SKeletonVideoPlayer.SKVPReader;
import org.talh.SKeletonVideoPlayer.SKVPSyntaxErrorException;
import org.talh.SKeletonVideoPlayer.SkeletonVideoFrame;
import org.talh.SKeletonVideoPlayer.graph.Rendered3DGraph;

public class PlayerBuffer {
	private ConcurrentLinkedQueue<Rendered3DGraph> buffer;
	//private boolean endOfData;
	//private SKVPReader reader;
	private Semaphore bufferAccess;
	private Semaphore firstBufferFilling;
	private Thread bufferFiller;
	private boolean bufferingStarted;
	private boolean dismissed = false;
	//private SKVPReader reader;
	
	public PlayerBuffer(SKVPReader reader, Long skipSize) {
		this.buffer = new ConcurrentLinkedQueue<Rendered3DGraph>();
		//this.reader = reader;
		//this.endOfData = false;
		//this.reader = reader;
		this.bufferAccess = new Semaphore(1, true);
		this.firstBufferFilling = new Semaphore(1, true);
		this.bufferingStarted = false;
		skipSize = (skipSize == null) ? 0 : skipSize;
		if (skipSize > 0) {
			try {
				reader.getNextFrames(skipSize.intValue());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SKVPNonInitializedReaderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SKVPSyntaxErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		this.bufferFiller = new Thread() {
			public void run() {
				acquireBufferNoExc();
				while (! dismissed) {					
					List<SkeletonVideoFrame> frames = null;
					try {
						frames = reader.getNextFrames(Defs.PLAYER_BUFFER_FILL_SIZE);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (SKVPNonInitializedReaderException e) {
						e.printStackTrace();
					} catch (SKVPSyntaxErrorException e) {
						e.printStackTrace();
					}
					for (SkeletonVideoFrame frame : frames) {
						Rendered3DGraph graph = null;
						try {
							graph = Utils.SKVPHeaderToGraph(reader);
						} catch (SKVPNonInitializedReaderException e) {
							e.printStackTrace();
						}
						Utils.updateGraphCoordinates(graph, frame);
						buffer.offer(graph);
						if (dismissed) {
							break;
						}
					}
					frames.clear();
					firstBufferFilling.release();
					// Waiting for the reader to let the buffer run once again
					acquireBufferNoExc();
				}
			}
		};
	}
	
	public Rendered3DGraph getNextFrameGraph() {
		if (! bufferingStarted) {
			bufferingStarted = true; 			
			acquireFirstFillingBufferNoExc();
			this.bufferFiller.start();
			// We won't be able to proceed until the buffer will finish its first iteration
			// and will release the semaphore
			acquireFirstFillingBufferNoExc();
		}
		// Letting buffer work again when the queue is filled with enough data
		//if (this.numFramesRead != 0 && this.numFramesRead % Defs.PLAYER_BUFFER_NUM_READS_TRIGGER == 0) {
		if (this.buffer.size() == Defs.PLAYER_BUFFER_MINIMAL_SIZE_TRIGGER) {
			bufferAccess.release();
		}
		
		// It returns null when the queue is empty, which is exactly what we want to happen
		// The queue can be empty only when there is no anymore data to read
		return this.buffer.poll();
	}
	
	public void dismissBuffer() {
		dismissed = true;
	}
	
	private void acquireBufferNoExc() {
		try {
			bufferAccess.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void acquireFirstFillingBufferNoExc() {
		try {
			firstBufferFilling.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
		
	
	
	
}
