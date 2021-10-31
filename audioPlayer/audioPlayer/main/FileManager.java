package audioPlayer.main;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import audioPlayer.main.audio.AudioState;
public class FileManager {
	public static Level defaultLogLevel = Level.OFF;
	private static Logger log =Logger.getLogger(FileManager.class.getName());
	private String dir;
	public FileManager() {
		log.setLevel(defaultLogLevel);
		dir = System.getProperty("user.dir");
		log.info("Starting FileManager with directory "+dir);
	}
	/**
	 * Change the directory to search musics, the default dir is from 'user.dir' property
	 * @param dir Directory to search, case null select 'user.dir' property
	 * */
	public void setDir(String dir) {
		if(new File(dir).isDirectory()) {
			log.info("Setting Directory to "+dir);
			this.dir = dir;
		}else {
			log.warning("Validation from directory has failed");
			if(dir == null)
				dir = System.getProperty("user.dir");
		}
	}
	public Music getMusic(FilePath path) {
		Music newMusic = new Music(path);
		return newMusic;
	}
	public Music getMusic(String path) {
		try {
			log.info("Getting Audio From "+path);
			FilePath filePath = new FilePath(path);
			return getMusic(filePath);
		} catch (InvalidPathFormatException e) {
			log.log(Level.WARNING,"Failed getting Audio",e);
			return null;
		}
	}
	/**
	 * Return musics from selected directory in setDir().
	 * */
	public Music[] getMusics() {
		String fileNames[] = getStringFilesFrom(dir);
		log.info("Getting all musics from "+dir);
		if(fileNames.length ==0)
			return new Music[0];
		ArrayList <Music> musics = new ArrayList<Music>();
		for(int index =0; index < fileNames.length;index++) {
			Music result = getMusic(fileNames[index]);
			if(result != null) {
				if(result.getState() == AudioState.SUCCESS || result.getState() == AudioState.NOT_STARTED)
					musics.add(result);
			}
		};
		return musics.toArray(new Music[musics.size()]);
	};
	/**
	 * get all file names in directory
	 * */
	private String[] getStringFilesFrom(String dir) {
		File actualDir = new File(dir);
		if(!actualDir.isDirectory())
			return new String[0];
		String names[] = actualDir.list();
		for(int index = 0; index < names.length;index++) {
			names[index] = dir+File.separatorChar+names[index];
		}
			
		return names;
	}
	protected void setLoggerParent(Logger logger) {
		FileManager.log.setParent(logger);
	}
	
}
