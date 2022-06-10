package engine.screens;

import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;
import engine.Creature;

public class CharacterScreen implements Screen {

    protected Creature player;

    public CharacterScreen(Creature player) {
        this.player = player;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
		int x = 80;

		terminal.clear(' ', x, 1, 45, 30);

		terminal.write("-- Character Information --", x, 1);
        terminal.write("Level: " + player.level(), x, 2);
        terminal.write("Experience: " + player.xp(), x, 3);
        terminal.write("Health: " + player.hp() + "/" + player.maxHp(), x, 4);
        terminal.write("Attack: " + player.attackValue(), x, 5);
        terminal.write("Defense: " + player.defenseValue(), x, 6);
        terminal.write("Food: " + player.food() + "/" + player.maxFood(), x, 7);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_ESCAPE || key.getKeyCode() == KeyEvent.VK_C) {
			return null;
		} else {
			return this;
        }
    }
}
