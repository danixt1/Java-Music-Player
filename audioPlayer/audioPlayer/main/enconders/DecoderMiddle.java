package audioPlayer.main.enconders;

import java.util.logging.Level;
import java.util.logging.Logger;

import audioPlayer.main.FilePath;
import audioPlayer.main.MusicInfo;
import audioPlayer.main.audio.AudioInterface;
public abstract class DecoderMiddle implements AudioInterface {
	public static Level defaultLogLevel =Level.OFF;
	protected String name;
	private FilePath path;
	DecoderMiddle(FilePath path){
		if(path == null)
			throw new NullPointerException();
		this.path = path;
	}
	public FilePath getPath() {
		return path;
	};
	public abstract MusicInfo getInfo();
	protected abstract void setLoggerParent(Logger parent);
}
