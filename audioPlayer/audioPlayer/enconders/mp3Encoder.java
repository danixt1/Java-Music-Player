package audioPlayer.enconders;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.JavaSoundAudioDevice;
import javazoom.jl.player.advanced.*;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import audioPlayer.audio.AudioListener;
import audioPlayer.audio.AudioState;
import audioPlayer.main.FilePath;
import audioPlayer.main.MusicInfo;
import audioPlayer.main.MusicInfoBuilder;

public class mp3Encoder extends Encoder {
	
	private AdvancedPlayer player;
	private InputStream stream;
	private int actualPart =0;
	private int lenght = 0;
	
	private boolean isPlaying = false;
	private boolean isPaused = false;
	
	private JavaSoundAudioDevice audioDevice;
	private AudioState state = AudioState.NOT_STARTED;
	private volatile AudioListener listener;
	private int volume =0;
	protected final float FRAME_RATE = 26.122449f;//ms
	
	private FilePath path;
	
	private MusicInfoBuilder info = new MusicInfoBuilder();
	
	public mp3Encoder(FilePath path){
		super(path);
		this.path = path;
		audioDevice = new JavaSoundAudioDevice();
		setHeader();
	};
	private void setHeader() {
		Mp3File mp3Reader = null;
		try {
			mp3Reader = new Mp3File(path.getAbsPath());
		} catch (UnsupportedTagException e) {
			setState(AudioState.PROCESSING_ERROR);
		} catch (InvalidDataException e) {
			setState(AudioState.NOT_AUDIO);
		} catch (IOException e) {
			setState(AudioState.UNKNOWN);
		}
		setState(AudioState.SUCCESS);
		lenght = (int) mp3Reader.getLengthInMilliseconds();
		info.setFileName(path.getFileName());
		info.setLenght(lenght);
		if(mp3Reader.hasId3v1Tag()) {
			ID3v1 tag =mp3Reader.getId3v1Tag();
			info.setArtist(tag.getArtist());
			info.setTitle(tag.getTitle());
		};
		if(mp3Reader.hasId3v2Tag()) {
			ID3v2 tag =mp3Reader.getId3v2Tag();
			info.setArtist(tag.getArtist());
			info.setTitle(tag.getTitle());
			info.setAlbumImage(tag.getAlbumImage());
			info.setYear(tag.getYear());
		};
	}
	private void execute() {
		try {
			stream = new FileInputStream(path.getAbsPath());
		} catch (FileNotFoundException e) {
			setState(AudioState.NOT_FOUND);
		}
		try {
			player = new AdvancedPlayer(stream,audioDevice);
		} catch (JavaLayerException e) {
			setState(AudioState.PROCESSING_ERROR);
		};
		generateListeners();
		try {
			setState(AudioState.SUCCESS);
			player.play(actualPart,Integer.MAX_VALUE);					
		} catch (JavaLayerException e) {
			setState(AudioState.PROCESSING_ERROR);
		};
	}
	@Override
	public void play() {
		if(isPlaying) {
			player.stop();
		}
		isPlaying = true;
		execute();
	}
	@Override
	public void pause() {
		isPlaying = false;
		isPaused = true;
		actualPart += convertToframes(audioDevice.getPosition());
		player.stop();
	}

	@Override
	public void stop() {
		if(player != null)
			player.stop();
		actualPart =0;
		isPlaying = false;
	}

	@Override
	public int getActualTime() {
		if(audioDevice != null)
			return convertToMilliseconds(actualPart)+ audioDevice.getPosition();
		else
			return convertToMilliseconds(actualPart);
	}

	@Override
	public void setTime(int t) {
		actualPart = convertToframes(t);
	}
	@Override
	public void setVolume(int volume) {
		this.volume = volume;
		if(audioDevice == null)
			return;
		//-80f  a 6f
		float converted = volume/100f * 86f - 80;
		audioDevice.setLineGain(converted);
	}

	@Override
	public int getVolume() {
		return volume;
	}
	@Override
	public synchronized boolean isPlaying() {
		return isPlaying;
	}

	@Override
	public int getLength() {
		return lenght;
	}
	protected void generateListeners() {
		player.setPlayBackListener(new PlaybackListener() {
			@Override
			public void playbackFinished(PlaybackEvent event) {
				if(listener != null && !isPaused)
					listener.audioFinished();
				isPlaying = false;
			};
			@Override
			public void playbackStarted(PlaybackEvent event) {
				isPaused = false;
				isPlaying = true;
				if(listener != null)
					listener.audioStarted();
			}
		});
	}
	protected int convertToframes(int miliseconds) {
		return (int) (miliseconds / FRAME_RATE);
	}
	protected int convertToMilliseconds(int frames) {
		return (int)(frames * FRAME_RATE);
	}
	@Override
	public MusicInfo getInfo() {
		return info.getMusicInfo();
	}
	@Override
	public AudioState getState() {
		return state;
	}
	private void setState(AudioState state) {
		this.state = state;
		if(listener != null) {
			if(this.state != state)
				listener.audioIoStateChange(state);
			if(state != AudioState.SUCCESS)
				listener.audioFailure(state);
		}
	}
	@Override
	public void setListener(AudioListener listener) {
		this.listener = listener;
	}
	@Override
	public AudioListener getListener() {
		return listener;
	}
	@Override
	public void play(int t) {
		setTime(t);
		play();
	}
}