package audioPlayer.main.audio;

public interface AudioInterface {
	public void play(int t);
	public void play();
	public void pause();
	public void stop();
	
	public int getActualTime();
	public void setTime(int t);
	public void setVolume(int volume);
	public int getVolume();
	public void setListener(AudioListener listener);
	public AudioListener getListener();
	public boolean isPlaying();
	public AudioState getState();
	public int getLength();
}
