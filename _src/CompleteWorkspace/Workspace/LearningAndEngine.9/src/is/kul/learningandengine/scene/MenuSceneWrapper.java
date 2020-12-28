package is.kul.learningandengine.scene;

import is.kul.learningandengine.SceneManager;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

public class MenuSceneWrapper extends AbstractScene implements IOnMenuItemClickListener {
	
	private IMenuItem playMenuItem;
	private MyTextMenuItemDecorator soundMenuItem;
	
	@Override
	public void populate() {

		MenuScene menuScene = new MenuScene(camera);
		menuScene.getBackground().setColor(0.82f, 0.96f, 0.97f);
		
	    playMenuItem = new ColorMenuItemDecorator(new TextMenuItem(0, res.font, "PLAY", vbom),
	    		Color.CYAN, Color.WHITE);
	    
	    soundMenuItem = new MyTextMenuItemDecorator(new TextMenuItem(1, res.font, getSoundLabel(), vbom),
	    		Color.CYAN, Color.WHITE);
	    
	    menuScene.addMenuItem(playMenuItem);
	    menuScene.addMenuItem(soundMenuItem);
	    
	    menuScene.buildAnimations();
	    menuScene.setBackgroundEnabled(true);
	    
	    menuScene.setOnMenuItemClickListener(this);		
	    
		Sprite player = new Sprite(240, 280, res.playerTextureRegion, vbom);
		menuScene.attachChild(player);
		
		Text hiscoreText = new Text(240, 600, res.font, "HISCORE: " + activity.getHiScore(), vbom);
		menuScene.attachChild(hiscoreText);
	    
	    setChildScene(menuScene);

	}

	private CharSequence getSoundLabel() {
		return activity.isSound() ? "SOUND ON" : "SOUND OFF";
	}

	@Override
	public void onPause() {
	}

	@Override
	public void onResume() {
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
		case 0 : 
			SceneManager.getInstance().showGameScene();
			return true;
		case 1 :
			boolean soundState = activity.isSound();
			soundState = !soundState;
			activity.setSound(soundState);
			soundMenuItem.setText(getSoundLabel());
			return true;
		default :
			return false;
		} 
	}

	@Override
	public void onBackKeyPressed() {
		activity.finish();
	}
	
	

}
