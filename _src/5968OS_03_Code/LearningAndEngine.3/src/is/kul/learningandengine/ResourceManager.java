package is.kul.learningandengine;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import android.graphics.Typeface;

public class ResourceManager {
	
	// single instance is created only
	private static final ResourceManager INSTANCE = new ResourceManager();
	
	//common objects
	public GameActivity activity;
	public Engine engine;
	public Camera camera;
	public VertexBufferObjectManager vbom;
	
	//font
	public Font font;
	
	//game textures
	public ITiledTextureRegion playerTextureRegion;
	public ITiledTextureRegion enemyTextureRegion;
	public ITextureRegion platformTextureRegion;
	public ITextureRegion cloud1TextureRegion;
	public ITextureRegion cloud2TextureRegion;
	
	private BuildableBitmapTextureAtlas gameTextureAtlas;	
	
	//sounds
	Sound soundFall;
	Sound soundJump;
	
	//music
	Music music;

	// constructor is private to ensure nobody can call it from outside
	private ResourceManager() { }
	
	public static ResourceManager getInstance() {
		return INSTANCE;
	}
	
	public void create(GameActivity activity, Engine engine, Camera camera, VertexBufferObjectManager vbom) {
		this.activity = activity;
		this.engine = engine;
		this.camera = camera;
		this.vbom = vbom;
	}
	
	public void loadGameGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");		
		gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 
				1024, 512, BitmapTextureFormat.RGBA_8888, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		playerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				gameTextureAtlas, activity.getAssets(), "player.png", 3, 1);
		
		enemyTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				gameTextureAtlas, activity.getAssets(), "enemy.png", 1, 2);
		
		platformTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				gameTextureAtlas, activity.getAssets(), "platform.png");		

		cloud1TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				gameTextureAtlas, activity.getAssets(), "cloud1.png");		

		cloud2TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				gameTextureAtlas, activity.getAssets(), "cloud2.png");		


		try {
			gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(2, 0, 2));
			gameTextureAtlas.load();
			
		} catch (final TextureAtlasBuilderException e) {
			throw new RuntimeException("Error while loading game textures", e);
		}			
	}
	
	public void loadGameAudio() {
		try {
			SoundFactory.setAssetBasePath("sfx/");
			soundJump = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "jump.ogg");
			soundFall = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "fall.ogg");
			
			MusicFactory.setAssetBasePath("mfx/");
			music = MusicFactory.createMusicFromAsset(activity.getMusicManager(), activity, "music.ogg");
		} catch (Exception e) {
			throw new RuntimeException("Error while loading audio", e);
		} 		
	}
	
	public void loadFont() {
		// starting from Android 4.4 the texture became too small for some reason
    // changed this to 512x256.
    font = FontFactory.createStroke(activity.getFontManager(), activity.getTextureManager(), 512, 256, Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD), 50,
				true, Color.WHITE_ABGR_PACKED_INT, 2, Color.BLACK_ABGR_PACKED_INT);
		font.load();
		font.unload();
	}
	
}
