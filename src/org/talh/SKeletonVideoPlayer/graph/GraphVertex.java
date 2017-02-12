package org.talh.SKeletonVideoPlayer.graph;

import java.util.HashMap;

import org.talh.SKeletonVideoPlayer.Coordinate3D;
import org.talh.SKeletonVideoPlayer.ElementColor;

public class GraphVertex {
	private Coordinate3D location;
	private HashMap<GraphVertex, GraphEdge> neighbors;
	private double radius;
	private ElementColor elementColor;
	
	public GraphVertex() {
		neighbors = new HashMap<GraphVertex, GraphEdge>();
	}
	
	
	public Coordinate3D getLocation() {
		return location;
	}
	
	public void setLocation(Coordinate3D location) {
		this.location = location;
	}
	
	public HashMap<GraphVertex, GraphEdge> getNeighbors() {
		return neighbors;
	}
	
	public void setNeighbors(HashMap<GraphVertex, GraphEdge> neighbors) {
		this.neighbors = neighbors;
	}
	
	public void addNeighbor(GraphVertex neighbor, ElementColor edgeColor, double edgeRadius) {
		if (neighbors.containsKey(neighbor)) {
			return;
		}
		GraphEdge edge = new GraphEdge();
		edge.setElementColor(edgeColor);
		edge.setRadius(edgeRadius);
		neighbors.put(neighbor, edge);
		neighbor.neighbors.put(this, edge);
	}


	public double getRadius() {
		return radius;
	}


	public void setRadius(double radius) {
		this.radius = radius;
	}


	public ElementColor getElementColor() {
		return elementColor;
	}


	public void setElementColor(ElementColor elementColor) {
		this.elementColor = elementColor;
	}
}

