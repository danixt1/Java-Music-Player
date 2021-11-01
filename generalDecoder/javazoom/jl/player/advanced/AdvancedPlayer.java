/*
 * 11/19/04		1.0 moved to LGPL. 
 *-----------------------------------------------------------------------
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Library General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *----------------------------------------------------------------------
 */

package javazoom.jl.player.advanced;

import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.SampleBuffer;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;

/**
 * a hybrid of javazoom.jl.player.Player tweeked to include <code>play(startFrame, endFrame)</code>
 * hopefully this will be included in the api
 */
public class AdvancedPlayer
{
	private Thread thread;
	private AtomicBoolean running = new AtomicBoolean(false);
	private AtomicBoolean isPaused = new AtomicBoolean(false);
	private int frames2 =0;
	/** The MPEG audio bitstream.*/
	private Bitstream bitstream;
	/** The MPEG audio decoder. */
	private Decoder decoder;
	/** The AudioDevice the audio samples are written to. */
	private AudioDevice audio;
	/** Has the player been closed? */
	private boolean closed = false;
	/** Has the player played back all frames from the stream? */

	/** Listener for the playback process */
	private PlaybackListener listener;
	/**
	 * Creates a new <code>Player</code> instance.
	 */
	public AdvancedPlayer(InputStream stream) throws JavaLayerException
	{
		this(stream, null);
	}

	public AdvancedPlayer(InputStream stream, AudioDevice device) throws JavaLayerException
	{
		bitstream = new Bitstream(stream);
		if (device!=null) audio = device;
		else audio = FactoryRegistry.systemRegistry().createAudioDevice();
		audio.open(decoder = new Decoder());
	}
	public void pause() {
		isPaused.set(!isPaused.get());
	};
	public void play() throws JavaLayerException
	{
		play(Integer.MAX_VALUE);
	}
	/**
	 * Plays a number of MPEG audio frames.
	 *
	 * @param frames	The number of frames to play.
	 * @return	true if the last frame was played, or false if there are
	 *			more frames.
	 * @throws JavaLayerException 
	 */
	public void play(int frames) throws JavaLayerException
	{
		thread =new Thread() {
			public void run() {
				if(listener != null) listener.playbackStarted(createEvent(PlaybackEvent.STARTED));
				frames2 = frames;
				running.set(true);
				isPaused.set(false);
				boolean ret = true;
				while (frames2 > 0 && ret && running.get())
				{
					try {
						if(!isPaused.get()) {
							frames2--;
							ret = decodeFrame();					
						};
					} catch (JavaLayerException e) {
						e.printStackTrace();
					}
				}
				{
					// last frame, ensure all data flushed to the audio device.
					AudioDevice out = audio;
					if (out != null)
					{
						out.flush();
						//System.out.println(audio.getPosition());
						synchronized (this)
						{
							close();
						}
						// report to listener
						if(listener != null) listener.playbackFinished(createEvent(out, PlaybackEvent.STOPPED));
					}
				}
			}
		};
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();
	}
	/**
	 * Cloases this player. Any audio currently playing is stopped
	 * immediately.
	 */
	public synchronized void close()
	{
		AudioDevice out = audio;
		if (out != null)
		{
			closed = true;
			audio = null;
			// this may fail, so ensure object state is set up before
			// calling this method.
			out.close();
			//lastPosition = out.getPosition();
			try
			{
				bitstream.close();
			}
			catch (BitstreamException ex)
			{}
		}
	}

	/**
	 * Decodes a single frame.
	 *
	 * @return true if there are no more frames to decode, false otherwise.
	 */
	protected boolean decodeFrame() throws JavaLayerException
	{
		try
		{
			AudioDevice out = audio;
			if (out == null) return false;

			Header h = bitstream.readFrame();
			if (h == null) return false;

			// sample buffer set when decoder constructed
			SampleBuffer output = (SampleBuffer) decoder.decodeFrame(h, bitstream);

			synchronized (this)
			{
				out = audio;
				if(out != null)
				{
					out.write(output.getBuffer(), 0, output.getBufferLength());
				}
			}

			bitstream.closeFrame();
		}
		catch (RuntimeException ex)
		{
			throw new JavaLayerException("Exception decoding audio frame", ex);
		}
		return true;
	}

	/**
	 * skips over a single frame
	 * @return false	if there are no more frames to decode, true otherwise.
	 */
	protected boolean skipFrame() throws JavaLayerException
	{
		Header h = bitstream.readFrame();
		if (h == null) return false;
		bitstream.closeFrame();
		return true;
	}

	/**
	 * Plays a range of MPEG audio frames
	 * @param start	The first frame to play
	 * @param end		The last frame to play
	 * @return true if the last frame was played, or false if there are more frames.
	 */
	public void play(final int start, final int end) throws JavaLayerException
	{
		boolean ret = true;
		int offset = start;
		while (offset-- > 0 && ret) ret = skipFrame();
		play(end - start);
	}

	/**
	 * Constructs a <code>PlaybackEvent</code>
	 */
	private PlaybackEvent createEvent(int id)
	{
		return createEvent(audio, id);
	}

	/**
	 * Constructs a <code>PlaybackEvent</code>
	 */
	private PlaybackEvent createEvent(AudioDevice dev, int id)
	{
		return new PlaybackEvent(this, id, dev.getPosition());
	}

	/**
	 * sets the <code>PlaybackListener</code>
	 */
	public void setPlayBackListener(PlaybackListener listener)
	{
		this.listener = listener;
	}

	/**
	 * gets the <code>PlaybackListener</code>
	 */
	public PlaybackListener getPlayBackListener()
	{
		return listener;
	}

	/**
	 * closes the player and notifies <code>PlaybackListener</code>
	 */
	public void stop()
	{	
		if(thread != null) {
			synchronized(thread) {
				
				if(running.get()) {
					running.set(false);
					try {
						thread.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		}
	}
}