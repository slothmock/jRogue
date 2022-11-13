package engine.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import asciiPanel.AsciiPanel;
import engine.Creature;
import engine.Item;

public class InventoryScreen implements Screen {

    private Creature player;

    public InventoryScreen(Creature player) {
        this.player = player;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        ArrayList<String> lines = getList();
		
		int y = 24;
		int x = 1;		

		terminal.clear(' ', x, y, 45, 6);
		String inventoryCap = String.format("- Backpack - : %d/%d - [Esc] to close menu", player.inventory().currentCapacity(), player.inventory().getItems().length);
        terminal.write(inventoryCap, x, y);

        if (lines.size() < 1) {
            terminal.write("You have nothing in your backpack.", x, y + 2);
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
			
			if (item == null)
				continue;
			
			String line = item.glyph() + " - " + item.name();
			
			if(item == player.weapon() || item == player.armor())
				line += " (equipped)";
			
			lines.add(line);
		}
		return lines;
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_ESCAPE: return null;
            case KeyEvent.VK_Q: return new QuaffScreen(player);
            case KeyEvent.VK_E: return new EatScreen(player);
            case KeyEvent.VK_D: return new DropScreen(player);
            case KeyEvent.VK_W: return new EquipScreen(player);
            case KeyEvent.VK_T: return new ThrowScreen(player, player.x, player.y);
            case KeyEvent.VK_I: return new InventoryScreen(player);
            case KeyEvent.VK_X: return new ExamineScreen(player);
            case KeyEvent.VK_H: return new GameHelpMenu(player);
            case KeyEvent.VK_C: return new CharacterScreen(player);
            
            default: return this;
        }
    }
}
