package audioPlayer.main;
import java.io.File;
import java.util.ArrayList;

import audioPlayer.audio.AudioState;
public class FileManager {
	private String dir;
	public FileManager() {
		dir = System.getProperty("user.dir");
		System.out.println("fileManger " +dir);
	}
	/**
	 * Change the directory to search musics, the default dir is from 'user.dir' property
	 * @param dir Directory to search, case null select 'user.dir' property
	 * */
	public void setDir(String dir) {
		if(new File(dir).isDirectory()) {
			this.dir = dir;
		}else {
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
			FilePath filePath = new FilePath(path);
			return getMusic(filePath);
		} catch (InvalidPathFormatException e) {
			return null;
		}
	}
	/**
	 * Return musics from selected directory in setDir().
	 * */
	public Music[] getMusics() {
		String fileNames[] = getStringFilesFrom(dir);
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
	
}
