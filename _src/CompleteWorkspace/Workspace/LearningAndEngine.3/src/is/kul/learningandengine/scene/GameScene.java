package is.kul.learningandengine.scene;

import is.kul.learningandengine.entity.Player;
import is.kul.learningandengine.factory.PlayerFactory;

import org.andengine.entity.Entity;
import org.andengine.entity.scene.background.EntityBackground;
import org.andengine.entity.sprite.Sprite;

public class GameScene extends AbstractScene {
	
	private Player player;
	
	public GameScene() {
		super();
		PlayerFactory.getInstance().create(vbom);
	}

	@Override
	public void populate() {
		createBackground();
		createPlayer();
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
		
	}

	@Override
	public void onResume() {
		
	}

}
