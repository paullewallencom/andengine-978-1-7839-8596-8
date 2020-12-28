package is.kul.learningandengine.scene;

import java.util.LinkedList;
import java.util.Random;

import is.kul.learningandengine.entity.Platform;
import is.kul.learningandengine.entity.Player;
import is.kul.learningandengine.factory.PlatformFactory;
import is.kul.learningandengine.factory.PlayerFactory;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.background.EntityBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.util.adt.align.HorizontalAlign;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;

public class GameScene extends AbstractScene implements IAccelerationListener {
	
	private Player player;
	
	private Text scoreText;
	
	private PhysicsWorld physicsWorld;
	
	Random rand = new Random();
	
	private LinkedList<Platform> platforms = new LinkedList<Platform>();	
	
	public GameScene() {
		super();
		physicsWorld = new PhysicsWorld(new Vector2(0, -SensorManager.GRAVITY_EARTH * 4), true);
		PlayerFactory.getInstance().create(physicsWorld, vbom);
		PlatformFactory.getInstance().create(physicsWorld, vbom);
	}
	
	@Override
	public void populate() {
		createBackground();
		createPlayer();
		createHUD();
		
		addPlatform(240, 100, false);
		
		engine.enableAccelerationSensor(activity, this);
		registerUpdateHandler(physicsWorld);	
	}
	
	private void addPlatform(float tx, float ty, boolean moving) {
		Platform platform;
		if (moving) {
			platform = PlatformFactory.getInstance().createMovingPlatform(tx, ty, (rand.nextFloat() - 0.5f) * 10f);
		} else {
			platform = PlatformFactory.getInstance().createPlatform(tx, ty);
		}
		attachChild(platform);
		platforms.add(platform);
	}

	private void createHUD() {
		HUD hud = new HUD();
		scoreText = new Text(16, 784, res.font, "0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
		scoreText.setAnchorCenter(0, 1);
		hud.attachChild(scoreText);
		camera.setHUD(hud);
	}	

	private void createPlayer() {
		player = PlayerFactory.getInstance().createPlayer(240, 400);
		attachChild(player);
	}

	private void createBackground() {
		Entity background = new Entity();
		background.setColor(0.44f, 0.56f, 0.9f);
		Sprite cloud1 = new Sprite(200, 300, res.cloud1TextureRegion, vbom);
		Sprite cloud2 = new Sprite(300, 600, res.cloud2TextureRegion, vbom);
		background.attachChild(cloud1);
		background.attachChild(cloud2);
		setBackground(new EntityBackground(0.82f, 0.96f, 0.97f, background));
	}

	@Override
	public void onPause() {
		engine.disableAccelerationSensor(activity);
	}

	@Override
	public void onResume() {
		engine.enableAccelerationSensor(activity, this);
	}

	@Override
	public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {
	}

	float lastX = 0;
	@Override
	public void onAccelerationChanged(final AccelerationData pAccelerationData) {
		if (Math.abs(pAccelerationData.getX() - lastX) > 0.5) {
			if (pAccelerationData.getX() > 0) {
				player.turnRight();
			} else {
				player.turnLeft();
			}
			lastX = pAccelerationData.getX();
		}
		
		final Vector2 gravity = Vector2Pool.obtain(pAccelerationData.getX() * 8, -SensorManager.GRAVITY_EARTH * 4);
		this.physicsWorld.setGravity(gravity);
		Vector2Pool.recycle(gravity);		

	}
}
