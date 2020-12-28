package is.kul.learningandengine.scene;

import is.kul.learningandengine.GameActivity;
import is.kul.learningandengine.entity.CollidableEntity;
import is.kul.learningandengine.entity.Enemy;
import is.kul.learningandengine.entity.Platform;
import is.kul.learningandengine.entity.Player;
import is.kul.learningandengine.factory.EnemyFactory;
import is.kul.learningandengine.factory.PlatformFactory;
import is.kul.learningandengine.factory.PlayerFactory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.EntityBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.align.HorizontalAlign;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

public class GameScene extends AbstractScene implements IAccelerationListener, IOnSceneTouchListener {
	
	private Player player;
	
	private Text scoreText;
	
	private PhysicsWorld physicsWorld;
	
	Random rand = new Random();
	
	private Text endGameText;
	
	private int score;
	
	private LinkedList<Platform> platforms = new LinkedList<Platform>();	
	private LinkedList<Enemy> enemies = new LinkedList<Enemy>();
	
	public GameScene() {
		super();
		physicsWorld = new PhysicsWorld(new Vector2(0, -SensorManager.GRAVITY_EARTH * 4), true);
		PlayerFactory.getInstance().create(physicsWorld, vbom);
		PlatformFactory.getInstance().create(physicsWorld, vbom);
		EnemyFactory.getInstance().create(physicsWorld, vbom);
	}

	private void addEnemy(float tx, float ty) {
		Enemy enemy = EnemyFactory.getInstance().createEnemy(tx, ty);
		attachChild(enemy);
		enemies.add(enemy);
	}	
	
	public static final short CATEGORY_BOX_1 = 1;
	public static final short CATEGORY_BOX_2 = 2;
	public static final short CATEGORY_CIRCLE = 4;
	public static final short CATEGORY_PLATFORM = 8;
	
	public static final short MASK_ALL = 
			CATEGORY_BOX_1 +
			CATEGORY_BOX_2 +
			CATEGORY_CIRCLE + 
			CATEGORY_PLATFORM;

	public static final short MASK_BOXES = 
			CATEGORY_CIRCLE +
			CATEGORY_PLATFORM;
	
	public static final short MASK_CIRCLE = 
			CATEGORY_CIRCLE +
			CATEGORY_PLATFORM;	
	
	
	@Override
	public void populate() { 
		createBackground();
		createPlayer();
		//camera.setChaseEntity(player);
		createHUD();
		
		addPlatform(240, 100, false);
		addPlatform(340, 400, false);
		addEnemy(140, 400);
		 
		engine.enableAccelerationSensor(activity, this);
		registerUpdateHandler(physicsWorld);	
		
		//physicsWorld.setContactListener(new MyContactListener(player));
		
		setOnSceneTouchListener(this);
//		
//		FixtureDef boxFixture1 = PhysicsFactory.createFixtureDef(1f, 0f, 2f, false,
//				CATEGORY_BOX_1, MASK_BOXES, (short) 1);
//		PhysicsFactory.createBoxBody(physicsWorld, 100, 300, 50, 20, BodyType.DynamicBody, boxFixture1);	
//		FixtureDef boxFixture2 = PhysicsFactory.createFixtureDef(1f, 0f, 2f, false,
//				CATEGORY_BOX_2, MASK_BOXES, (short) 1);
//		PhysicsFactory.createBoxBody(physicsWorld, 130, 350, 50, 20, BodyType.DynamicBody, boxFixture2);	
		
		FixtureDef circleFixture = PhysicsFactory.createFixtureDef(1f, 0f, 2f, false,
				CATEGORY_CIRCLE, MASK_CIRCLE, (short) 01);
		Body circle = PhysicsFactory.createCircleBody(physicsWorld, 80, 440, 25, BodyType.DynamicBody, circleFixture);	
		circle.setFixedRotation(false);
		
//		FixtureDef platformFixture = PhysicsFactory.createFixtureDef(1f, 0f, 2f, false,
//				CATEGORY_PLATFORM, MASK_ALL, (short) 0);
//		PhysicsFactory.createBoxBody(physicsWorld, 100, 250, 150, 15, BodyType.StaticBody, platformFixture);	
		
		RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
		revoluteJointDef.bodyA = player.getBody();
		revoluteJointDef.bodyB = circle;
		revoluteJointDef.localAnchorA.set(new Vector2(-1, 0));
		revoluteJointDef.localAnchorB.set(new Vector2(0, 0.6f));
		revoluteJointDef.collideConnected = false;
		revoluteJointDef.motorSpeed = 100f;
		revoluteJointDef.maxMotorTorque = 20f;
		revoluteJointDef.enableMotor = true;
		physicsWorld.createJoint(revoluteJointDef);
		
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
		score = 0;
		scoreText.setText(String.valueOf(score));
		hud.attachChild(scoreText);
		
		endGameText = new Text(GameActivity.CAMERA_WIDTH / 2, GameActivity.CAMERA_HEIGHT / 2,
				res.font, "GAME OVER! TAP TO CONTINUE", new TextOptions(HorizontalAlign.CENTER), vbom);
		endGameText.setAutoWrap(AutoWrap.WORDS);
		endGameText.setAutoWrapWidth(300f);
		endGameText.setVisible(false);
		hud.attachChild(endGameText);
		
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
	
	private static final float MIN = 50f;
	private static final float MAX = 250f;
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		boolean added = false;
		while (camera.getYMax() > platforms.getLast().getY()) {
			// x position of next platform
			float tx = rand.nextFloat() * GameActivity.CAMERA_WIDTH;
			// y position of next platform
			float ty = platforms.getLast().getY() + MIN + rand.nextFloat() * (MAX - MIN);
			// 10 % chance to add enemy on the platform
			if (rand.nextFloat() < 0.1) {
				addEnemy(tx, ty);
			}
			boolean moving = rand.nextBoolean();
			addPlatform(tx, ty, moving);
			added = true;
		}
		if (added) {
			sortChildren();
		}
		// player below last platform
		if (player.getY() < platforms.getFirst().getY()) {
			player.die();
		}
		cleanEntities(platforms, camera.getYMin());
		cleanEntities(enemies, camera.getYMin());
		calculateScore();
		
		if (player.isDead()) {
			endGameText.setVisible(true);
		}
	}
	
	private void calculateScore() {
		if (camera.getYMin() > score) {
			score = Math.round(camera.getYMin());
			scoreText.setText(String.valueOf(score));
		}
	}

	private void cleanEntities(List<? extends CollidableEntity> list, float bound) {
		Iterator<? extends CollidableEntity> iter = list.iterator();
		while (iter.hasNext()) {
			CollidableEntity ce = iter.next();
		    if (ce.getY() < bound) {
		    	iter.remove();
		    	ce.detachSelf();
		    	physicsWorld.destroyBody(ce.getBody());
		    }
		}		
	}	
	
	private void restartGame() {
		setIgnoreUpdate(true);
		unregisterUpdateHandler(physicsWorld);
		enemies.clear();
		platforms.clear();
		physicsWorld.clearForces();
		physicsWorld.clearPhysicsConnectors();
		while (physicsWorld.getBodies().hasNext()) {
			physicsWorld.destroyBody(physicsWorld.getBodies().next());
		}
		camera.reset();
		camera.setHUD(null);
		camera.setChaseEntity(null);
		detachChildren();
		
		populate();
		setIgnoreUpdate(false);		
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.isActionUp() && player.isDead()) {
				restartGame();
			return true;
		}
		return false;
	}
}
