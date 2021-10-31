package audioPlayer.main;
import java.util.logging.Level;
import java.util.logging.Logger;

import audioPlayer.main.audio.Audio;
import audioPlayer.main.enconders.*;

public class Music extends Audio{
	private static Level defaultLogLevel = Level.OFF;
	private String fileName;
	private DecoderMiddle encoder;
	private static Logger logger = Logger.getLogger(Music.class.getName());
	Music(FilePath path){
		logger.setLevel(defaultLogLevel);
		fileName = path.getFileName();
		switch(path.getFormat()) {
		case "mp3":
			logger.info("Encoder for mp3 file selected from "+path.getAbsPath());
			setEncoder(new Mp3DecoderMiddle(path));
			break;
		default:
			logger.info("Decoder Not Found "+path.getAbsPath());
			break;
		}
	}
	private void setEncoder(DecoderMiddle encoder) {
		this.encoder = encoder;
		setAudio(this.encoder);
	}
	public String getFileName() {
		return fileName;
	}
	public MusicInfo getMusicInfo() {
		return encoder.getInfo();
	}
	protected void setLoggerParent(Logger parent) {
		logger.setParent(parent);
	}
}
