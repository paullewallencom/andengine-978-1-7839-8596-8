package is.kul.learningandengine.scene;

import is.kul.learningandengine.R;
import is.kul.learningandengine.entity.Player;
import is.kul.learningandengine.factory.PlayerFactory;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.collision.CollisionHandler;
import org.andengine.engine.handler.collision.ICollisionCallback;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.EntityBackground;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseExponentialIn;

import android.widget.Toast;

public class GameScene extends AbstractScene implements IAccelerationListener {
	
	private Player player;
	
	private Text scoreText;
	
	AnimatedSprite fly;
	
	public GameScene() {
		super();
		PlayerFactory.getInstance().create(vbom);
		
	}
	
	@Override
	public void populate() {
		createBackground();
		createPlayer();
		createHUD();
		// This will create the toast and show it 
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Debug.i("Printing toast in Thread: " + Thread.currentThread().getName());
				Toast.makeText(activity, activity.getString(R.string.hello_world), Toast.LENGTH_LONG).show();
			}
		});
		
		// adding the fly object
		fly = new AnimatedSprite(240, 200, res.enemyTextureRegion, vbom) {

			// collision detection in onManagedUpdate
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {

				super.onManagedUpdate(pSecondsElapsed);
				if (collidesWith(player)) {
					setScale(2);
				} else {
					setScale(1);
				}
			}

		};
		// animate 2 frames, each frame 125ms
		fly.animate(125);
		attachChild(fly);
		// let it rotate
		fly.registerEntityModifier(new LoopEntityModifier(new RotationModifier(2, 0, 360, EaseExponentialIn.getInstance())));
		
		// scene touch listener to move player around
		setOnSceneTouchListener(new IOnSceneTouchListener() {
			
			@Override
			public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
				if (pSceneTouchEvent.isActionDown()) {
					Debug.i("Touching scene in Thread: " + Thread.currentThread().getName());
					IEntityModifierListener myEntityModifierListener = new IEntityModifierListener() {
						
						@Override
						public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
						}
						
						@Override
						public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
							Debug.i("Detaching player in Thread: " + Thread.currentThread().getName());
							res.activity.runOnUpdateThread(new Runnable() {
								@Override
								public void run() {
									player.detachSelf();									}
							});
							
						}
					};
					
					player.clearEntityModifiers();
					player.registerEntityModifier(new MoveModifier(1, player.getX(), player.getY(),
							pSceneTouchEvent.getX(), pSceneTouchEvent.getY(), myEntityModifierListener));
					return true;
				}
				return false;
			}
		});		
		
		registerTouchArea(player);
		engine.enableAccelerationSensor(activity, this);
		
		// collision callback for collision handler (fly x player)
		ICollisionCallback myCollisionCallback = new ICollisionCallback() {
			
			@Override
			public boolean onCollision(IShape pCheckShape, IShape pTargetShape) {
				fly.setColor(Color.RED);
				return false;
			}
		};
		
		CollisionHandler myCollisionHandler = new CollisionHandler(myCollisionCallback, fly, player);
		registerUpdateHandler(myCollisionHandler);
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
		
		player.setX(player.getX() + pAccelerationData.getX());
	}

	
	

}
