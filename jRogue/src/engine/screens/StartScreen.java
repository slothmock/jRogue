package engine.screens;

import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;

import engine.Difficulty;

public class StartScreen implements Screen {

	@Override
	public void displayOutput(AsciiPanel terminal) {
		terminal.writeCenter("jRogue - Another Classic Roguelike", 4);
		terminal.writeCenter("Difficulty: "+Difficulty.getDifficulty(), 10);
		terminal.writeCenter("-- press [Enter] to start --", 14);
		terminal.writeCenter("-- press [H] to see help screen --", 16);
		terminal.writeCenter("-- press [D] to change difficulty --", 18);
		terminal.writeCenter("-- press [Esc] to quit --", 20);

	}

	@Override
	public Screen respondToUserInput(KeyEvent key) {
		switch (key.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				//menuAudio.close();
				return new PlayScreen();
			case KeyEvent.VK_H:
				//menuAudio.close();
				return new HelpScreen();
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
			case KeyEvent.VK_D:
				Difficulty.setDifficulty();
			default:
				return this;
		}
	}
}
