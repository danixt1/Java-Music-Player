package audioPlayer.audio;

public enum AudioState {
	SUCCESS,
	NOT_FOUND,
	PROCESSING_ERROR,
	ADMIN_REQUIRE,
	UNKNOWN,//Like IO Exception
	NOT_STARTED,
	OCCUPIED,
	NOT_AUDIO //Probally
}
