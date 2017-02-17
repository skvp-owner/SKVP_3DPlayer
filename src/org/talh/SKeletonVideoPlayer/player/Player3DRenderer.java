package org.talh.SKeletonVideoPlayer.player;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.talh.SKeletonVideoPlayer.ElementColor;
import org.talh.SKeletonVideoPlayer.graph.GraphVertex;
import org.talh.SKeletonVideoPlayer.graph.Rendered3DGraph;

import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
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
	private double cameraLocationX;
	private double cameraLocationY;
	private double cameraLocationZ;
	private double cameraDestinationX;
	private double cameraDestinationY;
	private double cameraDestinationZ;
	Sphere[] spheres = null;
	Cylinder[] cylinders = null;

	private Camera camera;

	private Sphere[] watchingLineSpheres = null;

	public Player3DRenderer(Stage stage, double cameraLocationX, double cameraLocationY, double cameraLocationZ,
										double cameraDestinationX, double cameraDestinationY, double cameraDestinationZ) {
		parentScene = stage.getScene();
		this.cameraLocationX = cameraLocationX;
		this.cameraLocationY = (-1) * cameraLocationY;
		this.cameraLocationX = cameraLocationZ;
		this.cameraDestinationX = cameraDestinationX;
		this.cameraDestinationY = (-1) * cameraDestinationY;
		this.cameraDestinationZ = cameraDestinationZ;
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
		/*parentScene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			
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
					changeCameraDestination(cameraRotateX, cameraNewRotateY, cameraRotateZ);
					System.out.println("m");
				}
				if (arg0.getCode() == KeyCode.X) {
					double cameraNewRotateY = cameraRotateY + 2.0;
					changeCameraDestination(cameraRotateX, cameraNewRotateY, cameraRotateZ);
					System.out.println("m");
				}
				System.out.println(MessageFormat.format("Rotation: ({0},{1},{2})", cameraRotateX, cameraRotateY, cameraRotateZ));
				System.out.println(MessageFormat.format("Location: ({0},{1},{2})", cameraTranslateX, cameraTranslateY, cameraTranslateZ));
				
			}
			
		});*/
	}
/*
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
	}*/
/*
	public void clearGraphics() {
		graphicsContainer.getChildren().clear();
		//System.gc();
		setupCamera();
	}
	*/
	
	private void setupCamera() {
		
		camera = new PerspectiveCamera(true);
		graphicsContainer.getChildren().add(camera);
		subscene.setCamera(camera);
	//	}
		camera.getTransforms().clear();
		Point3D cameraDest = new Point3D(cameraDestinationX, cameraDestinationY, cameraDestinationZ);
		Point3D cameraOrig = new Point3D(cameraLocationX, cameraLocationY, cameraLocationZ);
		Point3D lookingVector = cameraDest.subtract(cameraOrig);
	/*	if (lookingVector.getZ() < 0) {
			Point3D yAxis = new Point3D(0, 1, 0);
			Rotate fixRot = new Rotate(180, yAxis);
			camera.getTransforms().add(fixRot);
		}*/
		Point3D cameraOrigDirection = new Point3D(0, 0, 1); // This is actually the Z axis
		double angle = Math.toDegrees(Math.acos(lookingVector.normalize().dotProduct(cameraOrigDirection)));
		Point3D rotAxis = cameraOrigDirection.crossProduct(lookingVector);
		System.out.println("ANGLE: " + angle);
		System.out.println("looking vector: " + lookingVector);
		if (lookingVector.getX() == 0 && lookingVector.getY() == 0 && lookingVector.getZ() < 0) {
			// Case when "looking vector" is parallel to Z axis
			// but is in the other trend, i.e. Z value is negative.
			rotAxis = new Point3D(0, 1, 0);
		} else if (lookingVector.getZ() < 0) {
			// Rotating camera around itself, as move to other side is going to flip it
			
		}
		System.out.println("angle: " + angle);
		Rotate rotate = new Rotate(angle, rotAxis);
		Translate moveToCameraLocation = new Translate(cameraLocationX, cameraLocationY, cameraLocationZ);
		camera.getTransforms().add(moveToCameraLocation);
		/*
			// flipping camera around Y axis so it looks at the correct place
			Point3D yAxis = new Point3D(0, 1, 0);
			Rotate fixRot = new Rotate(180, yAxis);
			camera.getTransforms().add(fixRot);
		}*/
		
		if (lookingVector.getZ() < 0 && (lookingVector.getX() != 0 || lookingVector.getY() != 0)) {
			Point3D selfFlipAxis = lookingVector;
			Rotate selfFlipFix = new Rotate(180, selfFlipAxis);
			camera.getTransforms().add(selfFlipFix);
		}
		camera.getTransforms().add(rotate);
		//camera.getTransforms().addAll(moveToCameraLocation, rotate);
		/*if (lookingVector.getZ() < 0) {
			Point3D aroundSelfRotationAxis = lookingVector.crossProduct(new Point3D(1, 0, 0));
			Rotate fixView = new Rotate(180, aroundSelfRotationAxis);
			camera.getTransforms().add(fixView);
		}*/
		
		
		//*** Uncomment it to debug watching engine ***		
		paintWatchingLine();
	}
	
	private void setupCameraOld2() {
		// TODO Auto-generated method stub
	//	if (camera == null) {
		camera = new PerspectiveCamera(true);
		graphicsContainer.getChildren().add(camera);
		subscene.setCamera(camera);
	//	}
		camera.getTransforms().clear();
		Point3D cameraDest = new Point3D(cameraDestinationX, cameraDestinationY, cameraDestinationZ);
		Point3D cameraOrig = new Point3D(cameraLocationX, cameraLocationY, cameraLocationZ);
		Point3D lookingVector = cameraDest.subtract(cameraOrig);
		Point3D lookingVectorProjOnXZ = new Point3D(lookingVector.getX(), 0, lookingVector.getZ());
		Point3D zAxis = new Point3D(0, 0, 1);
		// The denominator is 1 as first vector is normalized and Z axis is already a unit vector
		double angleWithZAxis = Math.acos(lookingVectorProjOnXZ.normalize().dotProduct(zAxis));
		if (cameraDestinationX - cameraLocationX > 0) {
			// We're on the other side of the Z axis. Need to rotate in opposite direction
			angleWithZAxis *= (-1);
		}
		System.out.println("angle with z: " + angleWithZAxis);
		Rotate rotationAroundY = new Rotate(-Math.toDegrees(angleWithZAxis), new Point3D(0, 1, 0));
		double angleBetweenLookingVecAndItsProjectionOnXZ = Math.acos(lookingVector.normalize().dotProduct(lookingVectorProjOnXZ.normalize()));
		if (cameraDestinationZ - cameraLocationZ < 0) {
			angleBetweenLookingVecAndItsProjectionOnXZ *= (-1);
		}
		System.out.println("looking vector: " + lookingVector);
		System.out.println("looking vector projected: " + lookingVectorProjOnXZ);
		System.out.println("angle with XZ: " + angleBetweenLookingVecAndItsProjectionOnXZ);
		Point3D rotationAxis = lookingVectorProjOnXZ.crossProduct(lookingVector);
		Rotate rotationForAngleWithProjectionOnXZ = new Rotate(Math.toDegrees(angleBetweenLookingVecAndItsProjectionOnXZ), rotationAxis);
		
		Translate moveToCameraLocation = new Translate(cameraLocationX, cameraLocationY, cameraLocationZ);
		
		camera.getTransforms().addAll(moveToCameraLocation, rotationAroundY, rotationForAngleWithProjectionOnXZ);
		
		
		// *** Uncomment it to debug watching engine ***		
		paintWatchingLine();
	}
	
	private void paintWatchingLine() {
		Point3D cameraDest = new Point3D(cameraDestinationX, cameraDestinationY, cameraDestinationZ);
		Point3D cameraOrig = new Point3D(cameraLocationX, cameraLocationY, cameraLocationZ);
		// Painting 10  0.1 radiused spheres every 1 units
		Point3D lookingUnitVector = cameraDest.subtract(cameraOrig).normalize();
		
		if (watchingLineSpheres == null) {
			watchingLineSpheres = new Sphere[10];
		}
		for (int i = 0 ; i < 10 ; i++) {
			Point3D sphereLocation = cameraOrig.add(lookingUnitVector.multiply(1 + i));
			System.out.println("sphere location: " + sphereLocation);
			if (watchingLineSpheres[i] == null) {
				watchingLineSpheres[i] = new Sphere(0.01);
				PhongMaterial material = new PhongMaterial();
				material.setDiffuseColor(Color.BLACK);
				material.setSpecularColor(Color.BLACK);
				watchingLineSpheres[i].setMaterial(material);
				graphicsContainer.getChildren().add(watchingLineSpheres[i]);
			}			
			watchingLineSpheres[i].setTranslateX(sphereLocation.getX());
			watchingLineSpheres[i].setTranslateY(sphereLocation.getY());
			watchingLineSpheres[i].setTranslateZ(sphereLocation.getZ());
		}
	}

	private void setupCameraOld() {
		// TODO Auto-generated method stub
	//	if (camera == null) {
			camera = new PerspectiveCamera(true);
			graphicsContainer.getChildren().add(camera);
			subscene.setCamera(camera);
	//	}
		camera.getTransforms().clear();
		
		Point3D zAxis = new Point3D(0, 0, 1);
		Point3D cameraDest = new Point3D(cameraDestinationX, cameraDestinationY, cameraDestinationZ);
		Point3D cameraOrig = new Point3D(cameraLocationX, cameraLocationY, cameraLocationZ);
	    Point3D diff = cameraDest.subtract(cameraOrig);
	    System.out.println("kuku: " + diff);
	    Point3D axisOfRotation = diff.crossProduct(zAxis);
	    double angle = Math.acos(diff.normalize().dotProduct(zAxis));
	    System.out.println("rot axis: " + axisOfRotation);
	    System.out.println("angle: " + angle);
	    
	    
	    Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);
	    Translate moveCameraToDesiredLocation = new Translate(cameraLocationX, cameraLocationY, cameraLocationZ);
	    camera.getTransforms().add(rotateAroundCenter);
	    camera.getTransforms().add(moveCameraToDesiredLocation);
	    if (cameraLocationZ > cameraDestinationZ) {
	    	Point3D yAxis = new Point3D(0, 1, 0);
	    	Rotate rotateCamera180 = new Rotate(180, yAxis);
	    	camera.getTransforms().add(rotateCamera180);
	    }
	    //camera.getTransforms().addAll(moveCameraToDesiredLocation, rotateAroundCenter);
	    //camera.setTranslateX(cameraLocationX);
	    //camera.setTranslateY(cameraLocationY);
	    //camera.setTranslateZ(cameraLocationZ);
	    //camera.setNearClip(0.1);
		//camera.setFarClip(2000.0);
		//camera.setFieldOfView(135);
		
		
	
		/*
		//camera.setTranslateZ(-200);
		//camera.setTranslateY(-250);
		//camera.setTranslateX(450);
		camera.getTransforms().addAll(new Rotate(cameraRotateZ, Rotate.Z_AXIS),
									new Rotate(cameraRotateX, Rotate.X_AXIS),
									new Rotate(cameraRotateY, Rotate.Y_AXIS));
		Translate moveToCameraLocation = new Translate(cameraTranslateX, -1 * cameraTranslateY, cameraTranslateZ);
		//camera.setTranslateZ(cameraTranslateZ);
		//camera.setTranslateY(-1 * cameraTranslateY);
		//camera.setTranslateX(cameraTranslateX);
		camera.getTransforms().clear();
		camera.getTransforms().addAll(moveToCameraLocation, new Rotate(cameraRotateY, Rotate.Y_AXIS));
		camera.setNearClip(0.1);
		camera.setFarClip(2000.0);
		camera.setFieldOfView(135);
		
	*/	
	}
	
	public void changeCameraLocation(double x, double y, double z) {
		this.cameraLocationX = x;
		this.cameraLocationY = -y;
		this.cameraLocationZ = z;
		setupCamera();
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

	public void changeCameraDestination(Double x, Double y, Double z) {
		cameraDestinationX = x;
		cameraDestinationY = -y;
		cameraDestinationZ = z;
		setupCamera();		
	}
	/*
	private double convertTo360Degrees(double angle) {
		while (angle < 0) {
			angle = 360 + angle; 
		}
		return (angle - (int)angle) + (int)angle % 360;
	}*/

}
