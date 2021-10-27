package audioPlayer.main;
import audioPlayer.audio.Audio;
import audioPlayer.enconders.*;

public class Music extends Audio{
	private String fileName;
	private Encoder encoder;
	Music(FilePath path){
		fileName = path.getFileName();
		switch(path.getFormat()) {
		case "mp3":
			setEncoder(new mp3Encoder(path));
			break;
		}
	}
	private void setEncoder(Encoder encoder) {
		this.encoder = encoder;
		setAudio(this.encoder);
	}
	public String getFileName() {
		return fileName;
	}
	public MusicInfo getMusicInfo() {
		return encoder.getInfo();
	}
}
