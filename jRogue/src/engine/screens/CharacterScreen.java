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
		int x = 88;
        int y = 3;
		terminal.clear(' ', x, y, 45, 20);

		terminal.write("-- Character Information --", x, y++);
        terminal.write("Level: " + player.level(), x, y++);
        terminal.write("Experience: " + player.xp(), x, y++);
        terminal.write("Health: " + player.hp() + "/" + player.maxHp(), x, y++);
        terminal.write("Attack: " + player.attackValue(), x, y++);
        terminal.write("Defense: " + player.defenseValue(), x, y++);
        terminal.write("Food: " + player.food() + "/" + player.maxFood(), x, y++);
        terminal.write("Mining: " + player.miningLevel(), x, y++);
        terminal.write("-- Press [C] to exit this menu --", x, y + 1);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_C) {
			return null;
		} else {
			return this;
        }
    }
}
