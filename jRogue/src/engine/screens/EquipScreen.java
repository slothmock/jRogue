package engine.screens;

import engine.Creature;
import engine.Item;

public class EquipScreen extends InventoryBasedScreen {

	public EquipScreen(Creature player) {
		super(player);
	}

	protected String getVerb() {
		return "equip";
	}

	protected boolean isAcceptable(Item item) {
		return item.attackValue() != 0 || item.defenseValue() != 0 || item.rangedAttackValue() != 0;
	}

	protected Screen use(Item item) {
		player.equip(item);
		return null;
	}
}
