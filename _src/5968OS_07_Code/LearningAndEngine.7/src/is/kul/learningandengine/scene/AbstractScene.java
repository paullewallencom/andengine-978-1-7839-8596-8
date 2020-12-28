/**
 * Copyright (C) 2014 Martin Varga <android@kul.is>
 */
package is.kul.learningandengine.scene;

import is.kul.learningandengine.GameActivity;
import is.kul.learningandengine.ResourceManager;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

/**
 * Base for all scenes, implements some default functionality.
 * 
 * @author Martin Varga
 */
public abstract class AbstractScene extends Scene {

	protected ResourceManager res = ResourceManager.getInstance();

	protected Engine engine = res.engine;
	protected GameActivity activity = res.activity;
	protected VertexBufferObjectManager vbom = res.vbom;
	protected Camera camera = res.camera;

	/**
	 * Populate the scene with objects
	 */
	public abstract void populate();

	/**
	 * Destroys all objects used by this scene
	 */
	public void destroy() {

	}

	/**
	 * Default action on back key is nothing
	 */
	public void onBackKeyPressed() {
		Debug.d("Back key pressed");
	}

	/**
	 * When the games is paused
	 */
	public abstract void onPause();

	/**
	 * When the game is resumed
	 */
	public abstract void onResume();
}
