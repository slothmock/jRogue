package engine.screens;

import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;
import engine.Creature;
import engine.Config;

public class GameHelpMenu implements Screen {

    protected Creature player;

    public GameHelpMenu(Creature player) {
        this.player = player;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
		int x = 82;
        int y = 3;
		terminal.clear(' ', x, y, 45, 20);

        for (int i = 0; i < Config.HELP_MENU_MSGLIST.length; i++) {
            terminal.write(Config.HELP_MENU_MSGLIST[i], x, y + i + 1);
        }

    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
			return null;
        } else {
            return this;
        }
    }
}
