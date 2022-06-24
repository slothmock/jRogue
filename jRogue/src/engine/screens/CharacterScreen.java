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
		int x = 23;
        int x2 = 46;
        int y = 25;
        
		terminal.clear(' ', x, y, 45, 10);

		terminal.write("-- Character  Information --", 26, y++);
        terminal.write("Level: " + player.level(), x, y++ + 1);
        terminal.write("Experience: " + player.xp(), x, y++ + 1);
        terminal.write("Health: " + player.hp() + "/" + player.maxHp(), x, y++ + 1);
        terminal.write("HP Regen: " + player.regenHpAmount(), x, y++ + 1);
        y = 26;
        terminal.write("Attack: " + player.attackValue(), x2, y++ + 1);
        terminal.write("Defense: " + player.defenseValue(), x2, y++ + 1);
        terminal.write("Food: " + player.food() + "/" + player.maxFood(), x2, y++ + 1);
        terminal.write("Mining: " + player.miningLevel(), x2, y++ + 1);
        terminal.write("-- Press [Esc] to exit this menu --", x, 32);
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
