package is.kul.learningandengine.factory;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.entity.Player;

import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class PlayerFactory {
	private static PlayerFactory INSTANCE = new PlayerFactory();
	private VertexBufferObjectManager vbom;
	private PhysicsWorld physicsWorld;
	public static final FixtureDef PLAYER_FIXTURE = PhysicsFactory.createFixtureDef(1f, 0f, 1f, false);	
	
	private PlayerFactory() {	}
	
	public static PlayerFactory getInstance() {
		return INSTANCE;
	}
	
	public void create(PhysicsWorld physicsWorld, VertexBufferObjectManager vbom) {
		this.physicsWorld = physicsWorld;
		this.vbom = vbom;
	}
	
	public Player createPlayer(float x, float y) {
		Player player = new Player(x, y, ResourceManager.getInstance().playerTextureRegion, vbom);
		player.setZIndex(2);
		
		Body playerBody = PhysicsFactory.createBoxBody(physicsWorld, player, 
				BodyType.DynamicBody, PLAYER_FIXTURE);
		playerBody.setLinearDamping(1f);
		playerBody.setFixedRotation(true);
		playerBody.setUserData(player);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(player, playerBody));
		
		player.setBody(playerBody);		
		return player;
	}
}
