package es.upm.fi.f980092.tfc.pacman.module.level;

import java.util.ArrayList;

import android.net.Uri;
import es.upm.fi.f980092.tfc.gameengine.ia.utils.pathfinder.PathFinder;
import es.upm.fi.f980092.tfc.gameengine.physics.PhysicsEnvironmentProperties;
import es.upm.fi.f980092.tfc.gameengine.scene.I_CameraMovement;
import es.upm.fi.f980092.tfc.gameengine.scene.Scene;
import es.upm.fi.f980092.tfc.gameengine.scene.cameras.CameraZoomFunction;
import es.upm.fi.f980092.tfc.gameengine.scene.cameras.ComplexCameraMovement;
import es.upm.fi.f980092.tfc.gameengine.scene.cameras.PersuivanCamera;
import es.upm.fi.f980092.tfc.pacman.engine.elements.GhostElement;
import es.upm.fi.f980092.tfc.pacman.engine.elements.PacmanElement;
import es.upm.fi.f980092.tfc.pacman.engine.elements.PillsNetElement;
import es.upm.fi.f980092.tfc.pacman.engine.elements.SceneElement;

public class Level extends Scene {
	
	/*****************
	 * Constantes
	 *****************/
	
//	private static final String TAG = "Pacman";
//	private static final String TAG2 = TAG + ":Level - ";
	
	private static final String SMALL_IMAGE = "Image1.png";
	private static final String BIG_IMAGE = "Image2.png";
	private static final float SCALE = 10000;
		
	/*********************
	 * Atributos
	 *********************/
	
	private Uri smallImageURI;			// Imagen pequeña del nivel 
	private Uri bigImageURI;			// Imagen grande del nivel 
		
	private PacmanElement pacman;		// Elemento pacman
	private SceneElement  scene; 		// Escenario
	private PillsNetElement pills;		// Listado de pastillas
	
	private I_CameraMovement startCamera;		// Camara cuando se inicia el juego que realiza un zoom
	private I_CameraMovement runningCamera;		// Camara normal del juego que sigue al pacman
	private I_CameraMovement restartCamera;		// Camara cuando muere el pacman girando el scenario

	private PathFinder pathFinder;
	
	/*****************
	 * Constructores
	 *****************/
	
	public Level(String path) {
		super(path, new PhysicsEnvironmentProperties( new int[]{0,0,-17}, (int) SCALE));
		smallImageURI = Uri.parse(path + SMALL_IMAGE);
		bigImageURI =   Uri.parse(path + BIG_IMAGE);
		
    	scene = new SceneElement();   	
    	pills  = new PillsNetElement();
    	pacman = new PacmanElement();
    	
    	pathFinder = new PathFinder();
    	
    	this.setScale(SCALE);
    	this.addElement(scene);
    	this.addElement(pacman);
    	this.addElement(pills);
    	
	}
	
	public void init() {
		pills.init();
		scene.init();
		pacman.init();
		pathFinder.initialize();
	}
	
	public void init(float aspect) {
		
		pacman.setScale(0.5f);
		 
		int x0 = (int) (pacman.getPosition()[0] / SCALE);
		int y0 = (int) (pacman.getPosition()[0] / SCALE);
				
		float[] p0 = new float[]{-x0,-y0, -100};
		float[] p1 = new float[]{-x0,-y0, -40};
		
		float[] r0 = new float[]{0.0f, 0.0f, 0.0f};
		float[] r1 = new float[]{45.0f, 0.0f, 0.0f};
		float[] r360 = new float[]{0.0f, 0.0f, 360.0f};
		
		float[] eye = new float[]{45,0,0};
		float fovy = 15;
		float near = 30;
		float far =  47;
		
		near = 30;
		far = 100;
		startCamera = new CameraZoomFunction(p0,p1,r0,r1,1500,aspect);
		runningCamera = new PersuivanCamera(pacman,SCALE, new float[]{0f, 0f,-40.0f}, eye, fovy, near , far, aspect);
		restartCamera = new CameraZoomFunction(p1, p0, r0, r360, 2000, aspect);
				
		setCamera(startCamera);
		super.init(aspect);				
	}
	/************************
	 * Métodos públicos
	 ************************/
	
	public Uri getBigScreenshot() {
		return bigImageURI;
	}
	
	public Uri getSmallImageURI() {
		return smallImageURI;		 
	}	
			
	public void setSmallImageURI(String uriString) {
		smallImageURI = Uri.parse(uriString);
	}

	public void setBigImageURI(String uriString) {
		bigImageURI = Uri.parse(uriString);
	}
		
	public int getNumberOfPill() {
		return pills.getNumberOfPills();
	}
	
	public PacmanElement getPacman() {
		return pacman;
	}

	public SceneElement getScene() {
		return scene;
	}

	public PillsNetElement getPills() {
		return pills;
	}
	
	public void addGhost(GhostElement ghost) {		
		this.addElement(ghost);
	}

	public I_CameraMovement getStartCamera() {
		return startCamera;
	}

	public void setStartCamera(I_CameraMovement startCamera) {
		this.startCamera = startCamera;
	}

	public I_CameraMovement getRunningCamera() {
		return runningCamera;
	}

	public void setRunningCamera(I_CameraMovement runningCamera) {
		this.runningCamera = runningCamera;
	}

	public I_CameraMovement getRestartCamera() {
		return restartCamera;
	}

	public void setRestartCamera(I_CameraMovement restartCamera) {
		this.restartCamera = restartCamera;
	}

	public PathFinder getPathFinder() {
		return pathFinder;
	}
	
}
