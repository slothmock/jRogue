package engine.screens;

import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;

public class MenuHelpScreen implements Screen {

	@Override
	public void displayOutput(AsciiPanel terminal) {
		terminal.writeCenter("-- Help Screen --", 2);
		terminal.writeCenter("Arrow Keys to move", 5);
		terminal.writeCenter("(C)haracter Information", 6);
		terminal.writeCenter("(D)rop an item", 7);
		terminal.writeCenter("(E)at an item", 8);
		terminal.writeCenter("(G)rab an item from the floor", 9);
		terminal.writeCenter("(W)ield/Wear an item", 10);
		
		
		terminal.writeCenter("-- Press [Enter] to go back --", 12);
	}

	@Override
	public Screen respondToUserInput(KeyEvent key) {
		return key.getKeyCode() == KeyEvent.VK_ENTER ? new StartScreen() : this;
	}
}