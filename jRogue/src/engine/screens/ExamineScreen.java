package engine.screens;

import engine.Creature;
import engine.Item;

public class ExamineScreen extends InventoryBasedScreen {

    public ExamineScreen(Creature player) {
        super(player);
    }

    protected String getVerb() {
        return "examine";
    }

    protected boolean isAcceptable(Item item) {
        return true;
    }

    protected Screen use(Item item) {
        player.notify("You examine the " + item.name() + ".");
        player.notify(item.name() + " - " + item.details());
        return null;
    }
}
