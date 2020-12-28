package is.kul.learningandengine.entity;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

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
	
}
