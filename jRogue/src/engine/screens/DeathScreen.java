package engine.screens;

import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;
import engine.Creature;

public class DeathScreen implements Screen {
	private Creature player;
	
	public DeathScreen(Creature player){
		this.player = player;
	}
	
	@Override
	public void displayOutput(AsciiPanel terminal) {
		terminal.writeCenter("GAME OVER", 3);
		terminal.writeCenter(player.causeOfDeath(), 6);
		terminal.writeCenter("-- Press [Enter] to restart --", 10);
		terminal.writeCenter("-- Press [Escape] to quitt --", 11);
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
