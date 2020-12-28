package is.kul.learningandengine.draft;


import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.IResolutionPolicy;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.BatchedSpriteParticleSystem;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.emitter.CircleParticleEmitter;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.AccelerationParticleInitializer;
import org.andengine.entity.particle.initializer.AlphaParticleInitializer;
import org.andengine.entity.particle.initializer.BlendFunctionParticleInitializer;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.initializer.RotationParticleInitializer;
import org.andengine.entity.particle.initializer.ScaleParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.OffCameraExpireParticleModifier;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.color.Color;

import android.opengl.GLES20;

public class GameActivity extends SimpleBaseGameActivity {
	
	public static final int CAMERA_WIDTH = 480;
	public static final int CAMERA_HEIGHT = 800;
	
	public static final int FPS_LIMIT = 30;
	
	ITexture smokeTex;
	ITextureRegion smokeTexReg;

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		Engine engine = new LimitedFPSEngine(pEngineOptions, FPS_LIMIT);
		engine.registerUpdateHandler(new FPSLogger());
		return engine;
	}
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		EngineOptions engineOptions;
		Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		IResolutionPolicy resolutionPolicy = new FillResolutionPolicy();
		engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, resolutionPolicy, camera);
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		engineOptions.getRenderOptions().setDithering(true);
		return engineOptions;
	}

	private void createSmoke(Scene scene) {
		final BatchedSpriteParticleSystem smokeParticleSystem = new BatchedSpriteParticleSystem(
				new CircleParticleEmitter(240, 400, 50), 
				20, 40, 300,
				smokeTexReg, getVertexBufferObjectManager());
		
		float ttl = 5.5f;

		smokeParticleSystem.addParticleInitializer(new VelocityParticleInitializer<UncoloredSprite>(-25, 25, 20, 60));
		smokeParticleSystem.addParticleInitializer(new AccelerationParticleInitializer<UncoloredSprite>(0, 20));
		smokeParticleSystem.addParticleInitializer(new ExpireParticleInitializer<UncoloredSprite>(ttl));
		smokeParticleSystem.addParticleInitializer(new ScaleParticleInitializer<UncoloredSprite>(0.1f, 0.5f));
		smokeParticleSystem.addParticleInitializer(new RotationParticleInitializer<UncoloredSprite>(0f, 360f));
		
		smokeParticleSystem.addParticleModifier(new OffCameraExpireParticleModifier<UncoloredSprite>(getEngine().getCamera()));
		smokeParticleSystem.addParticleModifier(new AlphaParticleModifier<UncoloredSprite>(0f, 0.5f, 0f, 0.2f));
		smokeParticleSystem.addParticleModifier(new AlphaParticleModifier<UncoloredSprite>(2f, ttl, 0.2f, 0f));
		scene.attachChild(smokeParticleSystem);
	}

	private void createFire(Scene scene) {
		IEntityFactory<Sprite> ief = new IEntityFactory<Sprite>() {
			
			@Override
			public Sprite create(float pX, float pY) {
				return new Sprite(pX, pY, smokeTexReg, getVertexBufferObjectManager());
			}
		};
		
		final ParticleSystem<Sprite> fireParticleSystem = new ParticleSystem<Sprite>(ief, 
				new PointParticleEmitter(240, 100), 
				20, 30, 200);
		fireParticleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));
		fireParticleSystem.addParticleInitializer(new ColorParticleInitializer<Sprite>(1f, 0.4f, 0.1f));
//		fireParticleSystem.addParticleInitializer(new ColorParticleInitializer<Sprite>(0.3f, 0.4f, 1f));
		fireParticleSystem.addParticleInitializer(new AlphaParticleInitializer<Sprite>(0f));
		
		fireParticleSystem.addParticleInitializer(new VelocityParticleInitializer<Sprite>(-15, 15, 20, 90));
		fireParticleSystem.addParticleInitializer(new ExpireParticleInitializer<Sprite>(4.5f));
		fireParticleSystem.addParticleInitializer(new ScaleParticleInitializer<Sprite>(0.5f));
		fireParticleSystem.addParticleInitializer(new RotationParticleInitializer<Sprite>(0f, 360f));
		
		fireParticleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(0f, 0.5f, 0f, 0.2f));
		fireParticleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(3f, 4.5f, 0.2f, 0f));
		fireParticleSystem.addParticleModifier(new ScaleParticleModifier<Sprite>(3f, 4.5f, 0.5f, 0f));
		scene.attachChild(fireParticleSystem);
	}	
	
	@Override
	protected void onCreateResources() throws IOException {
		smokeTex = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/smoke.png", TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		smokeTexReg = TextureRegionFactory.extractFromTexture(smokeTex);
		smokeTex.load();
	}
	@Override
	protected Scene onCreateScene() {
		Scene scene = new Scene();
		scene.getBackground().setColor(Color.BLACK);
		createSmoke(scene);
		createFire(scene);
		return scene;
	}
	
	
}
