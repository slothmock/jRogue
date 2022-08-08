package engine.screens;

import engine.Creature;
import engine.Item;

public class QuaffScreen extends InventoryBasedScreen {

        public QuaffScreen(Creature player) {
                super(player);
        }

        protected String getVerb() {
                return "drink";
        }

        protected boolean isAcceptable(Item item) {
                return item.quaffEffect() != null;
        }

        protected Screen use(Item item) {
                player.quaff(item);
                return null;
        }
}