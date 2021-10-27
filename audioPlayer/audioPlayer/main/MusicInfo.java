package audioPlayer.main;

public class MusicInfo {
	public String fileName = "";
	public String artist = "";
	public String title = "";
	public String year = "";
	public int lenghtInMiliseconds =0;
	public byte[] albumImage = new byte[0];
	
	@Override
	public String toString() {
		return "File Name:" + fileName +"\n" +
				"Title:" + title +"\n" +
				(artist != ""?  "Artist:" + artist +"\n":"") +
				(year.length() > 3 ? "year:" + year +"\n":"") +
				"Lenght: "+(lenghtInMiliseconds /1000) + " Seconds";
	}
}
