package audioPlayer.main;

public class FilePath {
	private String format;
	private String path;
	private String fileName;
	
	public FilePath(String path) throws InvalidPathFormatException{
		int end = path.length() -1;
		if (end < 3) {
			throw new InvalidPathFormatException();
		};
		if(path.lastIndexOf('.') == -1) {
			throw new InvalidPathFormatException();
		};
		format = path.substring(path.lastIndexOf('.') +1);
		int last = path.lastIndexOf('\\') + 1;
		fileName = path.substring(last);
		this.path = path;
	}
	public String getAbsPath() {
		return this.path;
	}
	public String getFileName() {
		return this.fileName;
	}
	public String getFormat() {
		return format;
	}
}
