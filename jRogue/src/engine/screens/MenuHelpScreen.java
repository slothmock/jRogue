package engine.screens;

import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;

public class MenuHelpScreen implements Screen {

	@Override
	public void displayOutput(AsciiPanel terminal) {
		int y = 3;
		terminal.writeCenter("-- Help Screen --", 1);
		terminal.writeCenter("Arrow Keys to move", y++);
		terminal.writeCenter("(C)haracter Information", y++);
		terminal.writeCenter("(D)rop an item", y++);
		terminal.writeCenter("(E)at an item", y++);
		terminal.writeCenter("(G)rab an item from the floor", y++);
		terminal.writeCenter("(W)ield/Wear an item", y++);
		terminal.writeCenter("Shift + > or < to use stairs", y++);
		terminal.writeCenter("Home/End to quit the game", y++);
		
		
		terminal.writeCenter("-- Press [Enter] to go back --", y + 2);
	}

	@Override
	public Screen respondToUserInput(KeyEvent key) {
		return key.getKeyCode() == KeyEvent.VK_ENTER ? new StartScreen() : this;
	}
}
