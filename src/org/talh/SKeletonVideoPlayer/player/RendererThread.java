package org.talh.SKeletonVideoPlayer.player;

import org.talh.SKeletonVideoPlayer.graph.Rendered3DGraph;

public class RendererThread implements Runnable {
	
	Rendered3DGraph graph;
	Player3DRenderer renderer;
	
	public RendererThread(Player3DRenderer renderer, Rendered3DGraph graph) {
		this.graph = graph;
		this.renderer = renderer;
	}
	
	@Override
	public void run() {
		renderer.paintSkeletonFrame(graph);
		graph = null;
		renderer = null;
	}

}
