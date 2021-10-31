package audioPlayer.main;

import java.util.logging.Level;
import java.util.logging.Logger;

import audioPlayer.main.audio.Audio;
import audioPlayer.main.audio.AudioListener;
/**
 * Principal class from the program.
 * */
public class Player extends Audio{
	/**
	 * Change the level from logger, the value from logger is changed in Constructor
	 * */
	public static Level defaultLogLevel =Level.OFF;
	private int volumeGlobal = 85;
	private Music[] musics;
	private FileManager fileManager;
	private Music actualMusic;
	private int actualPosition =0;
	private AudioListener audioListener;
	private static Logger logger = Logger.getLogger(Player.class.getName());
	
	public Player(){
		start();
		musics = fileManager.getMusics();
		if(musics != null) {
			setActualMusic(0);
		};
	};
	public Player(String dir) {
		start();
		setDir(dir);
	}
	private void start() {
		logger.setLevel(defaultLogLevel);
		fileManager = new FileManager();
		logger.info("Starting new Player");
	}
	public void setDir(String dir) {
		logger.info("setDir: Directory selected to:"+dir);
		fileManager.setDir(dir);
		musics = fileManager.getMusics();
		if(musics != null) {
			setActualMusic(0);
		}else
			logger.info("setDir: No Musics Found in directory");
	}
	private void setActualMusic(int musicIndex) {
		if(isPlaying())
			stop();
		if(musicIndex < musics.length && musicIndex > -1) {
			actualMusic = musics[musicIndex];
			logger.info("Selected Music:"+actualMusic.getFileName());
			actualMusic.setLoggerParent(logger);
			setAudio(actualMusic);
			actualPosition = musicIndex;
		}else {
			logger.warning("Failed to change music, invalid position");
		}
	};
	public int getVolume() {
		return volumeGlobal;
	}
	/**
	 *@param volume A value between 0 to 100
	 * */
	@Override
	public void setVolume(int volume) {
		logger.info("Volume adjusted from "+super.getVolume() +" to "+volume);
		super.setVolume(volume);
		volumeGlobal =super.getVolume();
	};
	@Override
	public void play() {
		if(!isPlaying()) {
			logger.info("Playing...");
			super.setListener(audioListener);
			super.play();						
			setVolume(volumeGlobal);
		}else
			logger.info("Play ignored, is already playing");
	}
	public MusicInfo getMusicInfo() {
		if(actualMusic != null)
			return actualMusic.getMusicInfo();
		return new MusicInfo();
	}
	public void nextMusic() {
		logger.info("Changing to next music...");
		setActualMusic(actualPosition + 1);
	};
	/**
	 * Set the callbacks from events
	 * @param listener the object with callbacks
	 * */
	@Override
	public void setListener(AudioListener listener) {
		logger.info("New Listener added");
		audioListener = listener;
		super.setListener(audioListener);
	}
	public void previousMusic() {
		setActualMusic(actualPosition -1);
	}
}
