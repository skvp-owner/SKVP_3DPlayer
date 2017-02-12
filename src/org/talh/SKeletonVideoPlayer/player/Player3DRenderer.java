package org.talh.SKeletonVideoPlayer.player;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.talh.SKeletonVideoPlayer.ElementColor;
import org.talh.SKeletonVideoPlayer.graph.GraphVertex;
import org.talh.SKeletonVideoPlayer.graph.Rendered3DGraph;

import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class Player3DRenderer extends HBox {
	
	private final static double TRANSLATION_SIZE = 0.1; 
	
	private Group graphicsContainer;
	private SubScene subscene;
	private Scene parentScene;
	private double cameraTranslateX;
	private double cameraTranslateY;
	private double cameraTranslateZ;
	private double cameraRotateX;
	private double cameraRotateY;
	private double cameraRotateZ;
	Sphere[] spheres = null;
	Cylinder[] cylinders = null;

	private PerspectiveCamera camera;

	public Player3DRenderer(Stage stage, double cameraX, double cameraY, double cameraZ,
										double cameraAngleX, double cameraAngleY, double cameraAngleZ) {
		parentScene = stage.getScene();
		cameraTranslateX = cameraX;
		cameraTranslateY = cameraY;
		cameraTranslateX = cameraZ;
		cameraRotateX = cameraAngleX;
		cameraRotateY = cameraAngleY;
		cameraRotateZ = cameraAngleZ;
		this.setStyle("-fx-padding: 10;" + 
                "-fx-border-style: solid inside;" + 
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" + 
                "-fx-border-radius: 5;" + 
                "-fx-border-color: blue;");
		graphicsContainer = new Group();
		subscene = new SubScene(graphicsContainer, 500, 500, true, SceneAntialiasing.BALANCED);
		//subscene.setRoot(graphicsContainer);
		this.prefWidthProperty().bind(stage.widthProperty());
		this.prefHeightProperty().bind(stage.heightProperty());
		this.getChildren().add(subscene);
		setupEvents();
		setupCamera();
	}
	
	private void setupEvents() {
		//graphicsContainer.add	
		parentScene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent arg0) {
				if (arg0.getCode() == KeyCode.D) {
					translateRightOrLeft(cameraRotateY, false);					
				}
				if (arg0.getCode() == KeyCode.A) {
					translateRightOrLeft(cameraRotateY, true);
				}
				if (arg0.getCode() == KeyCode.W) {
					double cameraNewTranslateY = cameraTranslateY + TRANSLATION_SIZE;
					changeCameraLocation(cameraTranslateX, cameraNewTranslateY, cameraTranslateZ);
				}
				if (arg0.getCode() == KeyCode.S) {
					double cameraNewTranslateY = cameraTranslateY - TRANSLATION_SIZE;
					changeCameraLocation(cameraTranslateX, cameraNewTranslateY, cameraTranslateZ);
				}
				if (arg0.getCode() == KeyCode.O) {
					// translating "in" or "out" by "translating right" with angle + 90
					translateRightOrLeft(convertTo360Degrees(cameraRotateY + 90), true);
				}
				if (arg0.getCode() == KeyCode.I) {
					// translating "in" or "out" by "translating right" with angle + 90
					translateRightOrLeft(convertTo360Degrees(cameraRotateY + 90), false);
				}
				if (arg0.getCode() == KeyCode.Z) {
					double cameraNewRotateY = cameraRotateY - 2.0;
					changeCameraAngle(cameraRotateX, cameraNewRotateY, cameraRotateZ);
					System.out.println("m");
				}
				if (arg0.getCode() == KeyCode.X) {
					double cameraNewRotateY = cameraRotateY + 2.0;
					changeCameraAngle(cameraRotateX, cameraNewRotateY, cameraRotateZ);
					System.out.println("m");
				}
				System.out.println(MessageFormat.format("Rotation: ({0},{1},{2})", cameraRotateX, cameraRotateY, cameraRotateZ));
				System.out.println(MessageFormat.format("Location: ({0},{1},{2})", cameraTranslateX, cameraTranslateY, cameraTranslateZ));
				
			}
			
		});
	}

	protected void translateRightOrLeft(double rotateY, boolean left) {
		double angle = (rotateY - (int)rotateY) + (int)rotateY % 90;
		int quarter = (int)rotateY / 90;
		System.out.println("Quarter: " + quarter);
		double angleInRadians = Math.toRadians(angle);
		double newTranslateX = cameraTranslateX;
		double newTranslateZ = cameraTranslateZ;
		double sideFactor = left ? (-1) : 1;
		switch (quarter) {
			case 0:
				newTranslateX += sideFactor * TRANSLATION_SIZE * Math.cos(angleInRadians);
				newTranslateZ += sideFactor * TRANSLATION_SIZE * Math.sin(angleInRadians);
				break;
			case 1:
				newTranslateX -= sideFactor * TRANSLATION_SIZE * Math.sin(angleInRadians);
				newTranslateZ += sideFactor * TRANSLATION_SIZE * Math.cos(angleInRadians);
				break;
			case 2:
				newTranslateX -= sideFactor * TRANSLATION_SIZE * Math.cos(angleInRadians);
				newTranslateZ -= sideFactor * TRANSLATION_SIZE * Math.sin(angleInRadians);
				break;
			case 3:
				newTranslateX += sideFactor * TRANSLATION_SIZE * Math.sin(angleInRadians);
				newTranslateZ -= sideFactor * TRANSLATION_SIZE * Math.cos(angleInRadians);
				break;
			default:
				System.err.println("Ilelgal quarter. Translation is cancelled");
				return;
		}
		changeCameraLocation(newTranslateX, cameraTranslateY, newTranslateZ);
	}

	public void clearGraphics() {
		graphicsContainer.getChildren().clear();
		//System.gc();
		setupCamera();
	}
	
	private void setupCamera() {
		// TODO Auto-generated method stub
		if (camera == null) {
			camera = new PerspectiveCamera(true);
			graphicsContainer.getChildren().add(camera);
			subscene.setCamera(camera);
		}
		//camera.setTranslateZ(-200);
		//camera.setTranslateY(-250);
		//camera.setTranslateX(450);
		/*camera.getTransforms().addAll(new Rotate(cameraRotateZ, Rotate.Z_AXIS),
									new Rotate(cameraRotateX, Rotate.X_AXIS),
									new Rotate(cameraRotateY, Rotate.Y_AXIS));*/
		Translate moveToCameraLocation = new Translate(cameraTranslateX, -1 * cameraTranslateY, cameraTranslateZ);
		//camera.setTranslateZ(cameraTranslateZ);
		//camera.setTranslateY(-1 * cameraTranslateY);
		//camera.setTranslateX(cameraTranslateX);
		camera.getTransforms().clear();
		camera.getTransforms().addAll(moveToCameraLocation, new Rotate(cameraRotateY, Rotate.Y_AXIS));
		camera.setNearClip(0.1);
		camera.setFarClip(2000.0);
		camera.setFieldOfView(135);
		
		
	}
	
	public void changeCameraLocation(double x, double y, double z) {
		this.cameraTranslateX = x;
		this.cameraTranslateY = y;
		this.cameraTranslateZ = z;
		setupCamera();
	}

	public void kpaintSkeletonFrame(Rendered3DGraph graph) {
		
	}
	
	public synchronized void paintSkeletonFrame(Rendered3DGraph graph) {
		//clearGraphics();
		HashSet<GraphVertex> renderedVertices = new HashSet<GraphVertex>();
		int edgeNum = 0;
		boolean spheresWasNull = false;
		if (spheres == null) {
			spheres = new Sphere[graph.getVertices().size()];
			spheresWasNull = true;
		}
		int vertexNum = -1;
		List<Cylinder> renderedCylinders = new LinkedList<Cylinder>();
		for(GraphVertex v : graph.getVertices()) {
			vertexNum++;
			if (spheresWasNull) {
				spheres[vertexNum] = new Sphere(v.getRadius());
				PhongMaterial material = new PhongMaterial();
				material.setDiffuseColor(v.getElementColor().getDiffuseColor());
				material.setSpecularColor(v.getElementColor().getSpecularColor());
				spheres[vertexNum].setMaterial(material);
				graphicsContainer.getChildren().add(spheres[vertexNum]);
			}
			Sphere s = spheres[vertexNum];			
			s.setTranslateX(v.getLocation().getX());
			s.setTranslateY(-1 * v.getLocation().getY());
			s.setTranslateZ(v.getLocation().getZ());
			//Cylinder c = new Cylinder(3, 10);
			
			
			//graphicsContainer.getChildren().add(c);
			for (GraphVertex n : v.getNeighbors().keySet()) {
				if (renderedVertices.contains(n)) {
					continue;
				}
				ElementColor edgeColor = v.getNeighbors().get(n).getElementColor();
				double edgeRadius = v.getNeighbors().get(n).getRadius();
				if (spheresWasNull) {
					Cylinder c = createEdge(v, n, edgeNum, edgeColor, edgeRadius, spheresWasNull);
					graphicsContainer.getChildren().add(c);
					renderedCylinders.add(c);
				} else {
					changeEdgeOrientation(v, n, cylinders[edgeNum]);
				}
				edgeNum++;
			//	double dist = getEucDist(v, n);
			//	System.out.println(dist);
				//Cylinder c = new Cylinder(9, dist);
				
			}
			renderedVertices.add(v);
			
		}
		if (spheresWasNull) {
			cylinders = renderedCylinders.toArray(new Cylinder[renderedCylinders.size()]);
		}
	}
	
	private void changeEdgeOrientation(GraphVertex v, GraphVertex n, Cylinder cylinder) {
		Point3D origin = new Point3D(v.getLocation().getX(), -v.getLocation().getY(), v.getLocation().getZ());
		Point3D target = new Point3D(n.getLocation().getX(), -n.getLocation().getY(), n.getLocation().getZ());
		Point3D yAxis = new Point3D(0, 1, 0);
	    Point3D diff = target.subtract(origin);
	    double height = diff.magnitude();

	    Point3D mid = target.midpoint(origin);
	    Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

	    Point3D axisOfRotation = diff.crossProduct(yAxis);
	    double angle = Math.acos(diff.normalize().dotProduct(yAxis));
	    Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

	    cylinder.setHeight(height);
	    cylinder.getTransforms().clear();
	    cylinder.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);		
	}

	private Cylinder createEdge(GraphVertex v, GraphVertex n, int edgeNum, ElementColor edgeColor, double edgeRadius, boolean spheresWasNull) {
		Point3D p1 = new Point3D(v.getLocation().getX(), -v.getLocation().getY(), v.getLocation().getZ());
		Point3D p2 = new Point3D(n.getLocation().getX(), -n.getLocation().getY(), n.getLocation().getZ());
		Cylinder c = createConnection(p1, p2, edgeColor, edgeRadius, edgeNum, spheresWasNull);
		
		
		return c;
	}
	
	
	
	private Cylinder createConnection(Point3D origin, Point3D target, ElementColor edgeColor, double edgeRadius, int edgeNum, boolean spheresWasNull) {
	    Point3D yAxis = new Point3D(0, 1, 0);
	    Point3D diff = target.subtract(origin);
	    double height = diff.magnitude();

	    Point3D mid = target.midpoint(origin);
	    Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

	    Point3D axisOfRotation = diff.crossProduct(yAxis);
	    double angle = Math.acos(diff.normalize().dotProduct(yAxis));
	    Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

	    Cylinder line = new Cylinder(edgeRadius, height);
	    PhongMaterial material = new PhongMaterial();
		material.setDiffuseColor(edgeColor.getDiffuseColor());
		material.setSpecularColor(edgeColor.getSpecularColor());
		line.setMaterial(material);

	    line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

	    return line;
	}

	public void changeCameraAngle(Double x, Double y, Double z) {
		cameraRotateX = convertTo360Degrees(x);
		cameraRotateY = convertTo360Degrees(y);
		cameraRotateZ = convertTo360Degrees(z);
		setupCamera();		
	}
	
	private double convertTo360Degrees(double angle) {
		while (angle < 0) {
			angle = 360 + angle; 
		}
		return (angle - (int)angle) + (int)angle % 360;
	}

}
