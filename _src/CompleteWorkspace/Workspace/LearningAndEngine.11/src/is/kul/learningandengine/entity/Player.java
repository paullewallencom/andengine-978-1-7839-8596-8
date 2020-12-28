package is.kul.learningandengine.entity;

import is.kul.learningandengine.ResourceManager;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import com.badlogic.gdx.physics.box2d.Body;

/** 
 * The Player class is a sprite with extra functionality.
 * It expects the underlying TiledSprite has 3 tiles:
 * jump, fall and die
 * @author Martin Varga
 *
 */
public class Player extends TiledSprite implements CollidableEntity {
	
	boolean dead = false;
	
	private Body body;
	public static final String TYPE = "Player";

	
	public Player(float pX, float pY,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
	}
	
	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}	
	
	public void turnLeft() {
		setFlippedHorizontal(true);
	}
	
	public void turnRight() {
		setFlippedHorizontal(false);
	}
	
	public void fly() {
		setCurrentTileIndex(0);
	}
	
	public void fall() {
		setCurrentTileIndex(1);
	}
	
	public void die() {
		if (!dead) {
			ResourceManager.getInstance().activity.playSound(ResourceManager.getInstance().soundFall);
		}
		setDead(true);
		setCurrentTileIndex(2);
	}
	
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {

		if (pSceneTouchEvent.isActionDown()) {
			Debug.i("Touching player in Thread: " + Thread.currentThread().getName());
			clearEntityModifiers();
			return true;
		} else if (pSceneTouchEvent.isActionMove()) {
			setPosition(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
			return true;
		}
		return false;
	}	
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		Utils.wraparound(this);
		// if somebody set we are dying, we can't switch anymore
		if (getCurrentTileIndex() < 2) {
			if (body.getLinearVelocity().y < 0) {
				fall();
			} else {
				fly();
			}
		}
	}
	
	@Override
	public void setBody(Body body) {
		this.body = body;
		
	}

	@Override
	public Body getBody() {
		return body;
	}

	@Override
	public String getType() {
		return TYPE;
	}	
	
}
