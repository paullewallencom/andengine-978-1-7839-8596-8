package is.kul.learningandengine.scene;

import is.kul.learningandengine.R;
import is.kul.learningandengine.entity.Player;
import is.kul.learningandengine.factory.PlayerFactory;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.background.EntityBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.util.adt.align.HorizontalAlign;

import android.widget.Toast;

public class GameScene extends AbstractScene {
	
	private Player player;
	
	private Text scoreText;
	
	public GameScene() {
		super();
		PlayerFactory.getInstance().create(vbom);
	}

	@Override
	public void populate() {
		createBackground();
		createPlayer();
		createHUD();
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(activity, activity.getString(R.string.hello_world), Toast.LENGTH_LONG).show();
			}
		});
	}

	private void createHUD() {
		HUD hud = new HUD();
		
		scoreText = new Text(16, 784, res.font, "0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
		scoreText.setAnchorCenter(0, 1);
		hud.attachChild(scoreText);
		
		camera.setHUD(hud);
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
