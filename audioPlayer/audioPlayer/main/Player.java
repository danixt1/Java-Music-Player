package audioPlayer.main;

import audioPlayer.audio.Audio;
import audioPlayer.audio.AudioListener;

public class Player extends Audio{
	
	private int volumeGlobal = 70;
	private Music[] musics;
	private FileManager fileManager;
	private Music actualMusic;
	private int actualPosition =0;
	private AudioListener audioListener;
	
	public Player(){
		fileManager = new FileManager();
		musics = fileManager.getMusics();
		if(musics != null) {
			setActualMusic(0);
		};
	};
	public Player(String dir) {
		fileManager = new FileManager();
		setDir(dir);
	}
	public void setDir(String dir) {
		fileManager.setDir(dir);
		musics = fileManager.getMusics();
		if(musics != null)
			setActualMusic(0);
	}
	private void setActualMusic(int musicIndex) {
		if(isPlaying())
			stop();
		if(musicIndex < musics.length) {
			actualMusic = musics[musicIndex];
			setAudio(actualMusic);
			actualPosition = musicIndex;
		}
	};
	public int getVolume() {
		return volumeGlobal;
	}
	@Override
	public void setVolume(int volume) {
		super.setVolume(volume);
		volumeGlobal =super.getVolume();
	};
	@Override
	public void play() {
		if(!isPlaying()) {
			super.setListener(audioListener);
			setVolume(volumeGlobal);
			super.play();						
		}
	}
	public MusicInfo getMusicInfo() {
		if(actualMusic != null)
			return actualMusic.getMusicInfo();
		return new MusicInfo();
	}
	public void nextMusic() {
		if(actualPosition < musics.length) {
			setActualMusic(actualPosition+1);
		}
	};
	@Override
	public void setListener(AudioListener listener) {
		audioListener = listener;
		super.setListener(audioListener);
	}
	public void previousMusic() {
		if(actualPosition > 0) {
			setActualMusic(actualPosition -1);
		}
	};
}
