package is.kul.learningandengine;

public class MusicPlayer {
	
	private static MusicPlayer INSTANCE = new MusicPlayer();
	
	private ResourceManager res;
	
	private MusicPlayer() {
		res = ResourceManager.getInstance();
	}
	
	public static MusicPlayer getInstance() {
		return INSTANCE;
	}
	
	public void play() {
		if (res.activity.isSound() && !res.music.isPlaying()) {
			res.music.play();
		}
	}
	
	public void pause() {
		if (res.music.isPlaying()) {
			res.music.pause();
		}
	}
	
	public void stop() {
		if (res.music.isPlaying()) {
			res.music.pause();
			res.music.seekTo(0);
		}
	}	
	
}
