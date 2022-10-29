package engine.screens;

import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import asciiPanel.AsciiPanel;
import engine.AudioPlayer;

public class StartScreen implements Screen {

	private AudioPlayer titleAudio;

	@Override
	public void displayOutput(AsciiPanel terminal) {
		terminal.writeCenter("jRogue - Another Classic Roguelike", 4);
		terminal.writeCenter("-- press [Enter] to start --", 14);
		terminal.writeCenter("-- press [H] to see help screen --", 16);
		terminal.writeCenter("-- press [Esc] to quit --", 18);

		playTitleAudio();
	}

	@Override
	public Screen respondToUserInput(KeyEvent key) {
		switch (key.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				titleAudio.close();
				return new PlayScreen();
			case KeyEvent.VK_H:
				titleAudio.close();
				return new HelpScreen();
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
			default:
				return this;
		}
	}

	public void playTitleAudio() {
		try {
			titleAudio = new AudioPlayer("titleAudio.wav");
		} catch (UnsupportedAudioFileException | IOException 
				| LineUnavailableException e) {
					e.printStackTrace();
		}

		if (titleAudio.isPlaying()) {
			titleAudio.stop();
			titleAudio.close();
		} else {
			titleAudio.loop();
		}
	}
}
