package org.talh.SKeletonVideoPlayer.player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.talh.SKeletonVideoPlayer.ElementColor;
import org.talh.SKeletonVideoPlayer.SKVPNonInitializedReaderException;
import org.talh.SKeletonVideoPlayer.SKVPReader;
import org.talh.SKeletonVideoPlayer.SkeletonVideoFrame;
import org.talh.SKeletonVideoPlayer.graph.GraphVertex;
import org.talh.SKeletonVideoPlayer.graph.Rendered3DGraph;

public class Utils {

	public static Rendered3DGraph SKVPHeaderToGraph(SKVPReader reader) throws SKVPNonInitializedReaderException {
		int numJoints = reader.getNumJoints();
		ElementColor[] jointColors = reader.getJointColors();
		ElementColor connectionsColor = reader.getConnectionsColor();
		double[] jointRadiuses = reader.getJointRadiuses();
		double connectionsRadius = reader.getConnectionsRadius();
		GraphVertex vertices[] = new GraphVertex[numJoints];
		Rendered3DGraph graph = new Rendered3DGraph();
		for (int i = 0 ; i < numJoints ; i++) {
			vertices[i] = new GraphVertex();
			vertices[i].setElementColor(jointColors[i]);
			vertices[i].setRadius(jointRadiuses[i]);
			graph.addVertex(vertices[i]);
		}
		HashMap<Integer, HashSet<Integer>> connections = reader.getJointConnections();
		for (Integer vertexId : connections.keySet()) {
			for (Integer neighborId : connections.get(vertexId)) {
				vertices[vertexId - 1].addNeighbor(vertices[neighborId - 1], connectionsColor, connectionsRadius);
			}
		}	
		
		return graph;
	}
	
	public static void updateGraphCoordinates(Rendered3DGraph graph, SkeletonVideoFrame frame) {
		List<GraphVertex> vertices = graph.getVertices();
		int vertexNum = 0;
		for (GraphVertex vertex : vertices) {
			vertex.setLocation(frame.getCoordinate(vertexNum + 1));
			vertexNum++;
		}
	}
}
