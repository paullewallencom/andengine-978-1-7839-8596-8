package is.kul.learningandengine;

import org.andengine.util.debug.Debug;

import android.os.AsyncTask;
import is.kul.learningandengine.scene.AbstractScene;
import is.kul.learningandengine.scene.GameScene;
import is.kul.learningandengine.scene.LoadingScene;
import is.kul.learningandengine.scene.MenuSceneWrapper;
import is.kul.learningandengine.scene.SplashScene;

public class SceneManager {
	
	// single instance is created only
	private static final SceneManager INSTANCE = new SceneManager();
	public static final long SPLASH_DURATION = 2000;
	
	private ResourceManager res = ResourceManager.getInstance();
	
	private AbstractScene currentScene;
	
	private LoadingScene loadingScene = null;
	
	
	private SceneManager() { }

	public static SceneManager getInstance() {
		return INSTANCE;
	}	


	public AbstractScene getCurrentScene() {
		return currentScene;
	}
	
	
	public void setCurrentScene(AbstractScene currentScene) {
		this.currentScene = currentScene;
		res.engine.setScene(currentScene);
		Debug.i("Current scene: " + currentScene.getClass().getName())	;	
	}	
	
	public AbstractScene showSplashAndMenuScene() {
		final SplashScene splashScene = new SplashScene();
		splashScene.populate();
		setCurrentScene(splashScene);
		
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				long timestamp = System.currentTimeMillis();
				res.loadFont();
				res.loadGameAudio();
				res.loadGameGraphics();
				
				loadingScene = new LoadingScene();
				loadingScene.populate();
				
				AbstractScene nextScene = new MenuSceneWrapper();

				if (System.currentTimeMillis() - timestamp < SPLASH_DURATION) {
					try {
						Thread.sleep(SPLASH_DURATION - (System.currentTimeMillis() - timestamp) );
					} catch (InterruptedException e) {
						Debug.e("Interrupted", e);
					}
				}			
				nextScene.populate();
				setCurrentScene(nextScene);
				splashScene.destroy();
				res.unloadSplashGraphics();
				return null;
			}
		}.execute();	
		return splashScene;
	}

	
	public void showMenuScene() {
		final AbstractScene previousScene = getCurrentScene();
		setCurrentScene(loadingScene);
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				MenuSceneWrapper menuSceneWrapper = new MenuSceneWrapper();
				menuSceneWrapper.populate();
				setCurrentScene(menuSceneWrapper);
				previousScene.destroy();
				return null;
			}
			
		}.execute();			

	}
	
	public void showGameScene() {
		final AbstractScene previousScene = getCurrentScene();
		setCurrentScene(loadingScene);
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Debug.e("Interrupted", e);
				}				
				GameScene gameScene = new GameScene();
				gameScene.populate();
				previousScene.destroy();

				setCurrentScene(gameScene);
				return null;
			}
			
		}.execute();			

	}
	
}
