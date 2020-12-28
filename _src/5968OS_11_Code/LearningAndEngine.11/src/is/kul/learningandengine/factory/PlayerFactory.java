package is.kul.learningandengine.factory;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.entity.Player;

import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

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
	
	
	private void createHead(Body body) {
		FixtureDef headFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f, 1f, true);
		CircleShape circle = new CircleShape();
		circle.setRadius(32 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
		circle.setPosition(new Vector2(0, 12 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
		headFixtureDef.shape = circle;
		body.createFixture(headFixtureDef);
	}
	
	private void createTorso(Body body) {
		FixtureDef torsoFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f, 0.5f, true);
		
		PolygonShape middleBox = new PolygonShape();

		final float halfWidth = 30 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		final float halfHeight = 8 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		final float yShift =  - 30 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		Vector2[] vertices = new Vector2[4];
		vertices[0] = new Vector2(halfWidth, halfHeight + yShift);
		vertices[1] = new Vector2(-halfWidth, halfHeight + yShift);
		vertices[2] = new Vector2(-halfWidth * 0.75f, -halfHeight + yShift);
		vertices[3] = new Vector2(halfWidth * 0.75f, -halfHeight + yShift);
		middleBox.set(vertices);
		
		torsoFixtureDef.shape = middleBox;
		
		body.createFixture(torsoFixtureDef);
	}
	
	
	private void createLegs(Body body) {
		FixtureDef legsFixtureDef = PhysicsFactory.createFixtureDef(4f, 0.2f, 1f, false);	
		
		PolygonShape legsBox = new PolygonShape();
		legsBox.setAsBox(20 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
				4 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
				new Vector2(0, -44 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT),
						0);
		legsFixtureDef.shape = legsBox;
		
		body.createFixture(legsFixtureDef);
	}	
	
	public Player createPlayer(float x, float y) {
		Player player = new Player(x, y, ResourceManager.getInstance().playerTextureRegion, vbom);
		player.setZIndex(2);
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.x = x / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		bodyDef.position.y = y / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		
		Body playerBody = physicsWorld.createBody(bodyDef);
		
		createTorso(playerBody);
		createHead(playerBody);
		createLegs(playerBody);

		playerBody.setLinearDamping(1f);
		playerBody.setFixedRotation(true);
		playerBody.setUserData(player);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(player, playerBody));
		
		player.setBody(playerBody);		
		return player;
	}
}
