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
		terminal.writeCenter(player.causeOfDeath(), 8);
		terminal.writeCenter("-- Press [Enter] to restart --", 22);
	}

	@Override
	public Screen respondToUserInput(KeyEvent key) {
		return key.getKeyCode() == KeyEvent.VK_ENTER ? new PlayScreen() : this;
	}
}
