import static org.junit.Assert.*;

import org.junit.Test;

import audioPlayer.main.FilePath;
import audioPlayer.main.InvalidPathFormatException;

public class FilePathTest {

	@Test
	public void stringPathWith3Chars() {
		assertThrows(InvalidPathFormatException.class,()->{
			new FilePath(".ad");
		});
	}
	@Test
	public void stringPathWithNoEndPoint() {
		assertThrows(InvalidPathFormatException.class,()->{
			new FilePath("sdasdawq");
		});
	}

}
