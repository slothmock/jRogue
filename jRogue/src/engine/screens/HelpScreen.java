package engine.screens;

import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import engine.Config;
import asciiPanel.AsciiPanel;
import engine.AudioPlayer;

public class HelpScreen implements Screen {

	private AudioPlayer menuAudio;

	@Override
	public void displayOutput(AsciiPanel terminal) {
		int y = 5;

		terminal.writeCenter("-- Help Menu --", 2);

		for (int i = 2; i < Config.HELP_MENU_MSGLIST.length - 1; i++) {
			terminal.writeCenter(Config.HELP_MENU_MSGLIST[i], y++);
		}

		terminal.writeCenter("-- Press [Enter] to go back --", y + 2);

		playMenuAudio();
	}

	@Override
	public Screen respondToUserInput(KeyEvent key) {
		if (key.getKeyCode() == KeyEvent.VK_ENTER) {
			menuAudio.close();
			return new StartScreen();
		} else {
			return this;
		}
	}

	public void playMenuAudio() {
		try {
			menuAudio = new AudioPlayer("menuAudio.wav");
		} catch (UnsupportedAudioFileException | IOException
				| LineUnavailableException e) {
			e.printStackTrace();
		}

		if (menuAudio.isPlaying()) {
			menuAudio.stop();
			menuAudio.close();
		} else {
			menuAudio.loop();
		}
	}
}
