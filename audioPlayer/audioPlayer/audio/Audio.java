package audioPlayer.audio;

public class Audio implements AudioInterface {
	private AudioInterface audio;
	private boolean haveAudio = false;
	
	protected void setAudio(AudioInterface audio) {
		if(isPlaying())
			stop();
		this.audio = audio;
		haveAudio = audio == null ? false : true;
	}
	@Override
	public void play() {
		if(haveAudio)
			audio.play();			
	}
	@Override
	public void pause() {
		if(haveAudio)
			audio.pause();
	}
	@Override
	public void stop() {
		if(haveAudio)
			audio.stop();			
	}
	@Override
	public int getActualTime() {
		if(haveAudio)
			return audio.getActualTime();
		return -1;
	}
	@Override
	public void setTime(int t) {
		if(haveAudio) {
			audio.setTime(t);
		}
	}
	@Override
	public void setVolume(int volume) {
		if(volume > 100)
			volume =100;
		if(volume < 0)
			volume =0;
		if(haveAudio)
			audio.setVolume(volume);
	}
	@Override
	public boolean isPlaying() {
		if(haveAudio)
			return audio.isPlaying();
		return false;
	}
	/**
	 * Get Total Length is ms
	 * */
	@Override
	public int getLength() {
		if(haveAudio)
			return audio.getLength();
		return -1;
	}
	@Override
	public AudioState getState() {
		if(haveAudio)
			return audio.getState();
		return null;
	}
	@Override
	public void setListener(AudioListener listener) {
		if(haveAudio)
			audio.setListener(listener);
	}
	@Override
	public AudioListener getListener() {
		if(haveAudio)
			return audio.getListener();
		else
			return null;
	}
	@Override
	public int getVolume() {
		if(haveAudio)
			return audio.getVolume();
		else
			return -1;
	}
	@Override
	public void play(int t) {
		if(haveAudio)
			audio.play(t);
	}
}
