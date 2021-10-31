import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;

import audioPlayer.main.*;
import audioPlayer.main.audio.AudioListener;
import audioPlayer.main.audio.AudioState;
import audioPlayer.main.enconders.DecoderMiddle;

public class Start {
	private static boolean exit = false;
	private static Scanner inputer;
	private static String dir = System.getProperty("user.dir");
	private static Player player;
	public static void main(String[] args) {
		player = new Player(dir);
		System.out.println("Actual dir: "+dir);
		player.setListener(new AudioListener() {
			@Override
			public void audioFinished() {
				System.out.println("Audio ended");
			};
			@Override
			public void audioIoStateChange(AudioState a) {
				if(a != AudioState.SUCCESS)
				System.out.println("Error processing audio:"+a);
			}
		});
		actions(new String[] {"help"});
		try {
			while(!exit)
				startOutput();	
		}catch(Exception e) {
			e.printStackTrace();
			inputer.nextInt();
		}
	};
	private static void actions(String[] args) {
		args[0] = args[0].toLowerCase();
		switch(args[0]) {
			case "set-dir":
				if(args.length > 1)
					player.setDir(args[1]);
				System.out.println("Directory selected to "+args[1]);
				break;
			case "play":
				if(args.length > 1) {
					try {
						int value = Integer.parseInt(args[1]);		
						player.play(value);
					}catch(Exception e) {
						if(args[1] != null) {
							dir = args[1];
						}						
					}
				}else {
					player.play();					
				}
				System.out.println("Playing: "+player.getMusicInfo().title);
				break;
			case "help":
				System.out.println("Welcome to music player!\n");
				System.out.println("Commands:");
				System.out.println(
						"\tplay: play a music in the selected directory \n"
						+"\tplay <Integer>: Play a music in selected position(ms)\n"
						+"\tvolume <Integer>: Set volume levels 0 -100\n"
						+"\tset-time <Integer>: Set the time in milliseconds \n"
						+"\tpause: Pause\n"
						+"\tnext: Select next music\n"
						+"\tback: Select previous music\n"
						+"\tmusic-info: Return all infos from the music\n"
						+"\tset-dir <String> Select the directory to search musics"
						+"\thelp: Display this message\n"
						+ "\texit: Exit application");
				break;
			case "info":
				System.out.println("Using: \n\t jLayer 1.0 \n\t Mp3Agic by @mpatric source: https://github.com/mpatric/mp3agic");
				System.out.println("Created by @danixt1");
				System.out.println("respositore url: https://github.com/danixt1/Java-Music-Player");
				System.out.println("MIT License");
				break;
			case "exit":
				exit = true;
				break;
			case "stop":
				player.stop();
				System.out.println("Stopped");
				break;
			case "pause":
				player.pause();
				System.out.println("Paused");
				break;
			case "back":
				player.previousMusic();
				System.out.println("Selected:" + player.getMusicInfo().title);
				break;
			case "next":
				player.nextMusic();
				System.out.println("Selected:" + player.getMusicInfo().title);
				break;
			case "volume":
				if(args.length > 1) {
					try {
						int value = Integer.parseInt(args[1]);
						player.setVolume(value);
						System.out.println("Volume changed to "+player.getVolume());
					}catch(Exception e) {
						
						System.out.println("Invalid Number");
					}
				}else
					System.out.println("Actual Volume: "+player.getVolume());
				break;
			case "music-info":
				System.out.println(player.getMusicInfo().toString());
				if(player.isPlaying())
					System.out.println("Actual position: "+player.getActualTime() + " ms");
				break;
		}
	};

	private static void startOutput() {
		ArrayList<String> coms = new ArrayList<String>();
		inputer = new Scanner(System.in);
		String text = inputer.nextLine();
		int lastValidPos =0;
		int lastPosition = 0;
		while((lastPosition =text.indexOf(" ",lastPosition) + 1) != 0) {
			String textPart = text.substring(lastValidPos,lastPosition -1);
			if(contChars(textPart,"\"") % 2 == 0) {
				lastValidPos = lastPosition;
				coms.add(textPart.replaceAll("\"",""));
			};
		};
		if(lastValidPos > 0 && lastValidPos != text.length()-1) {
			coms.add(text.substring(lastValidPos).replaceAll("\"",""));
		}
		if(coms.size() ==0 && text.length() > 0)
			coms.add(text);
		if(coms.size() > 0)
			actions(coms.toArray(new String[coms.size()]));
	};
	private static int contChars(String text,String charToSearch) {
		int lastPosition = 0;
		int totChars =0;
		while((lastPosition =text.indexOf(charToSearch,lastPosition) + 1) != 0) {
			totChars++;
		}
		return totChars;
	};
}
