package audioPlayer.enconders;

import audioPlayer.audio.AudioInterface;
import audioPlayer.main.FilePath;
import audioPlayer.main.MusicInfo;
public abstract class Encoder implements AudioInterface {
	protected String name;
	private FilePath path;
	Encoder(FilePath path){
		if(path == null)
			throw new NullPointerException();
		this.path = path;
	}
	public FilePath getPath() {
		return path;
	};
	public abstract MusicInfo getInfo();
}
