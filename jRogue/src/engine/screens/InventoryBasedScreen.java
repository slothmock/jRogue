package engine.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import asciiPanel.AsciiPanel;
import engine.Creature;
import engine.Item;

public abstract class InventoryBasedScreen implements Screen {

	protected Creature player;
	private String letters;
	
	protected abstract String getVerb();
	protected abstract boolean isAcceptable(Item item);
	protected abstract Screen use(Item item);
	
	public InventoryBasedScreen(Creature player){
		this.player = player;
		this.letters = "abcdefghijklmnopqrstuvwxyz";
	}
	
	public void displayOutput(AsciiPanel terminal) {
		ArrayList<String> lines = getList();
		
		int y = 24;
		int x = 1;		

		terminal.clear(' ', x, y, 45, 6);
		terminal.write("What would you like to " + getVerb() + "? [Esc] to close menu", x, y);

		if (lines.size() < 1) {
			terminal.write("You have nothing to " + getVerb() + ".", x, y + 2);
		} else {
			for (String line : lines) {
				terminal.write(line, x, y++ + 2);
			}
		}
		
		terminal.repaint();
	}
	
	private ArrayList<String> getList() {
		ArrayList<String> lines = new ArrayList<String>();
		Item[] inventory = player.inventory().getItems();
		
		for (int i = 0; i < inventory.length - 1; i++){
			Item item = inventory[i];
			
			if (item == null || !isAcceptable(item))
				continue;
			
			String line = letters.charAt(i) + ": " + item.glyph() + " - " + item.name();
			
			if(item == player.weapon() || item == player.armor() || item == player.rangedWeapon())
				line += " (equipped)";

			if(getVerb() == "eat"){
				line += " (Food: "+item.foodValue()+")";
			}

			if(getVerb() == "equip"){
				line += " (Attack: "+item.attackValue()+", Ranged: "+item.rangedAttackValue()+", Defense: "+item.defenseValue()+")";
			}
			
			lines.add(line);
		}
		return lines;
	}

	public Screen respondToUserInput(KeyEvent key) {
		char c = key.getKeyChar();

		Item[] items = player.inventory().getItems();
		
		if (letters.indexOf(c) > -1 
				&& items.length > letters.indexOf(c)
				&& items[letters.indexOf(c)] != null
				&& isAcceptable(items[letters.indexOf(c)])) {
			return use(items[letters.indexOf(c)]);
		} else if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
			return null;
		} else {
			return this;
		}
	}
}
