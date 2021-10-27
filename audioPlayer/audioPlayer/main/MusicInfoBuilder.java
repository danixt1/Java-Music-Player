package audioPlayer.main;

public class MusicInfoBuilder {
	
	private String fileName = "";
	private String artist = "";
	private String title = "";
	private String year = "0";
	private int lenghtInMiliseconds =0;
	private byte[] albumImage;
	
	public void setArtist(String artist) {
		this.artist = artist == null ?"": artist;
	};
	public void setTitle(String title) {
		this.title = title == null ? "": title;
	};
	public void setYear(String year) {
		this.year = year == null ? "":year;
	};
	public void setFileName(String fileName) {
		this.fileName = fileName == null ? "":fileName;
		if(title == "")
			title = fileName;
	};
	
	public void setAlbumImage(byte[] image) {
		this.albumImage = image;
	};
	public void setLenght(int mili) {
		lenghtInMiliseconds = mili;
	};
	public MusicInfo getMusicInfo() {
		MusicInfo info =  new MusicInfo();
		info.artist = artist;
		info.fileName = fileName;
		info.title = title;
		info.year = year;
		info.albumImage = albumImage;
		info.lenghtInMiliseconds = lenghtInMiliseconds;
		return info;
	}
}
