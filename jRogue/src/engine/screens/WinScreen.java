package engine.screens;

import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;

public class WinScreen implements Screen {

	@Override
	public void displayOutput(AsciiPanel terminal) {
		terminal.writeCenter("You found your cactus and escaped the caves!", 3);
		terminal.writeCenter("-- press [Enter] to restart --", 5);
		terminal.writeCenter("-- press [Escape] to quit --", 6);
	}

	@Override
	public Screen respondToUserInput(KeyEvent key) {
		switch (key.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				return new PlayScreen();
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
			default:
				return this;
		}
	}
}
