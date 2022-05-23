package engine.screens;

import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;

public class StartScreen implements Screen {

	@Override
	public void displayOutput(AsciiPanel terminal) {
		terminal.writeCenter("jRogue - Another Classic Roguelike", 4);
		terminal.writeCenter("-- press [Enter] to start --", 14);
		terminal.writeCenter("-- press [H] to see help screen --", 15);
		terminal.writeCenter("-- press [Esc] to quit --", 16);
	}

	@Override
	public Screen respondToUserInput(KeyEvent key) {
		switch (key.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				return new PlayScreen();
			case KeyEvent.VK_H:
				return new HelpScreen();
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
			default:
				return this;
		}
	}
}
