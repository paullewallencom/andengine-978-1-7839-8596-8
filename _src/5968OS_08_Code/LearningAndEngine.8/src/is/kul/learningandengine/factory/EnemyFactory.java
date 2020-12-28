package is.kul.learningandengine.factory;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.entity.Enemy;

import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class EnemyFactory {
	public static final FixtureDef ENEMY_FIXTURE = PhysicsFactory.createFixtureDef(1f, 0f, 1f, true);
	
	private static EnemyFactory INSTANCE = new EnemyFactory();
	private PhysicsWorld physicsWorld;
	private VertexBufferObjectManager vbom;
	
	private EnemyFactory() {	}
	
	public static EnemyFactory getInstance() {
		return INSTANCE;
	}
	
	public void create(PhysicsWorld physicsWorld, VertexBufferObjectManager vbom) {
		this.physicsWorld = physicsWorld;
		this.vbom = vbom;
	}
	
	public Enemy createEnemy(float x, float y) {
		Enemy enemy = new Enemy(x, y, ResourceManager.getInstance().enemyTextureRegion, vbom);
		
		Body enemyBody = PhysicsFactory.createBoxBody(physicsWorld, enemy, 
				BodyType.KinematicBody, ENEMY_FIXTURE);
		
		enemyBody.setUserData(enemy);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(enemy, enemyBody));
		
		enemyBody.setLinearVelocity(-1, 0);
		enemy.setBody(enemyBody);
		enemy.animate(75);
		enemy.setZIndex(1);
		return enemy;
	}
}
