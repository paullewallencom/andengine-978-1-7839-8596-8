package is.kul.learningandengine.factory;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.entity.Platform;

import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.Constants;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class PlatformFactory {
	
	public static final FixtureDef PLATFORM_FIXTURE = PhysicsFactory.createFixtureDef(0f, 0f, 1f, false);
	
	private static PlatformFactory INSTANCE = new PlatformFactory();
	private PhysicsWorld physicsWorld;
	private VertexBufferObjectManager vbom;
	
	private PlatformFactory() {	}
	
	public static PlatformFactory getInstance() {
		return INSTANCE;
	}
	
	public void create(PhysicsWorld physicsWorld, VertexBufferObjectManager vbom) {
		this.physicsWorld = physicsWorld;
		this.vbom = vbom;
	}
	
	public Platform createPlatform(float x, float y) {
		Platform platform = new Platform(x, y, ResourceManager.getInstance().platformTextureRegion, vbom);
		platform.setAnchorCenterY(1);
		
		final float[] sceneCenterCoordinates = platform.getSceneCenterCoordinates();
		final float centerX = sceneCenterCoordinates[Constants.VERTEX_INDEX_X];
		final float centerY = sceneCenterCoordinates[Constants.VERTEX_INDEX_Y];
		
		Body platformBody = PhysicsFactory.createBoxBody(physicsWorld,
				centerX, centerY, 
				platform.getWidth() - 20, 1,
				BodyType.KinematicBody, PLATFORM_FIXTURE);
		platformBody.setUserData(platform);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(platform, platformBody));
		platform.setBody(platformBody);
		return platform;
	}
	
	public Platform createMovingPlatform(float x, float y, float velocity) {
		Platform platform = createPlatform(x, y);
		platform.getBody().setLinearVelocity(velocity, 0);
		return platform;
	}
}
