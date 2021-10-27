package audioPlayer.audio;

public abstract class AudioListener {
	public void audioFinished() {};
	public void audioStarted() {};
	
	public void audioFailure(AudioState audio) {};
	public void audioIoStateChange(AudioState audio) {};
}
