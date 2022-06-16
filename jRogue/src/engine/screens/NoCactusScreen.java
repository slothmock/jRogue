package engine.screens;

import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;

public class NoCactusScreen implements Screen {

	@Override
	public void displayOutput(AsciiPanel terminal) {
		terminal.writeCenter("You tried to leave without your cactus..", 3);
		terminal.writeCenter("-- press [Enter] to restart --", 5);
	}

	@Override
	public Screen respondToUserInput(KeyEvent key) {
		return key.getKeyCode() == KeyEvent.VK_ENTER ? new PlayScreen() : this;
	}
}
