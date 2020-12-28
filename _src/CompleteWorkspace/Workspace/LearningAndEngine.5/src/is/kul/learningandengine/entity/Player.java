package is.kul.learningandengine.entity;

import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

/** 
 * The Player class is a sprite with extra functionality.
 * It expects the underlying TiledSprite has 3 tiles:
 * jump, fall and die
 * @author Martin Varga
 *
 */
public class Player extends TiledSprite {
	
	boolean dead = false;
	
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
	
	
}
