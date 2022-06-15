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
		int x = 88;
        int y = 3;
		terminal.clear(' ', x, y, 45, 30);

		terminal.write("-- Help Screen --", x, y++);
		terminal.write("Arrow Keys to move", x, y++);
		terminal.write("(C)haracter Information", x, y++);
		terminal.write("(D)rop an item", x, y++);
		terminal.write("(E)at an item", x, y++);
		terminal.write("(G)rab from the ground", x, y++);
		terminal.write("(W)ield/Wear an item", x, y++);

        //TODO: Add stairs to help menu


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
