package is.kul.learningandengine.factory;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.entity.Player;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class PlayerFactory {
	private static PlayerFactory INSTANCE = new PlayerFactory();
	private VertexBufferObjectManager vbom;
	
	private PlayerFactory() {	}
	
	public static PlayerFactory getInstance() {
		return INSTANCE;
	}
	
	public void create(VertexBufferObjectManager vbom) {
		this.vbom = vbom;
	}
	
	public Player createPlayer(float x, float y) {
		Player player = new Player(x, y, ResourceManager.getInstance().playerTextureRegion, vbom);
		player.setZIndex(2);
		return player;
	}
}
