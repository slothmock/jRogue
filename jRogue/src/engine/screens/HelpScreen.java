package engine.screens;

import java.awt.event.KeyEvent;

import engine.Config;
import asciiPanel.AsciiPanel;


public class HelpScreen implements Screen {


	@Override
	public void displayOutput(AsciiPanel terminal) {
		int y = 5;

		terminal.writeCenter("-- Help Menu --", 2);

		for (int i = 2; i < Config.HELP_MENU_MSGLIST.length - 1; i++) {
			terminal.writeCenter(Config.HELP_MENU_MSGLIST[i], y++);
		}

		terminal.writeCenter("-- Press [Enter] to go back --", y + 2);


	}

	@Override
	public Screen respondToUserInput(KeyEvent key) {
		if (key.getKeyCode() == KeyEvent.VK_ENTER) {
			return new StartScreen();
		} else {
			return this;
		}
	}
}
