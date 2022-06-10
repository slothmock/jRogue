package engine.screens;

import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;
import engine.Creature;

public class GameHelpScreen implements Screen {

    protected Creature player;

    public GameHelpScreen(Creature player) {
        this.player = player;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
		int x = 80;

		terminal.clear(' ', x, 1, 45, 30);

		terminal.write("-- Help Screen --", x, 1);
		terminal.write("Arrow Keys to move", x, 3);
		terminal.write("(C)haracter Information", x, 4);
		terminal.write("(D)rop an item", x, 5);
		terminal.write("(E)at an item", x, 6);
		terminal.write("(G)rab from the ground", x, 7);
		terminal.write("(W)ield/Wear an item", x, 8);



    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_ESCAPE || key.getKeyCode() == KeyEvent.VK_H) {
			return null;
		} else {
			return this;
        }
    }
}
