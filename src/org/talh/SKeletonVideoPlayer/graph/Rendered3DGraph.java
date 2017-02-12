package org.talh.SKeletonVideoPlayer.graph;

import java.util.ArrayList;
import java.util.List;

public class Rendered3DGraph {
	private List<GraphVertex> vertices;
	
	public Rendered3DGraph() {
		vertices = new ArrayList<GraphVertex>();
	}	
	
	public List<GraphVertex> getVertices() {
		return vertices;
	}

	public void setVertices(List<GraphVertex> vertices) {
		this.vertices = vertices;
	}
	
	public void addVertex(GraphVertex v) {
		vertices.add(v);
	}
	
	public int getNumVertices() {
		return vertices.size();
	}
	
}
