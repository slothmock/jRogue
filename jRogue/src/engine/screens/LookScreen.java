package engine.screens;

import engine.Creature;
import engine.Item;
import engine.Tile;

public class LookScreen extends TargetBasedScreen {

    public LookScreen(Creature player, String caption, int sx, int sy) {
        super(player, caption, sx, sy);
    }

    public void enterWorldCoordinate(int x, int y, int screenX, int screenY) {
        Creature creature = player.creature(x, y, player.z);
        if (creature != null){
            caption = String.format("%s - %s - %s" ,
                        creature.glyph(), creature.name(), creature.details());
            return;
        }
    
        Item item = player.item(x, y, player.z);
        if (item != null){
            caption = String.format("%s - %s - %s", 
                        item.glyph(), item.name(), item.details());
            return;
        }
    
        Tile tile = player.tile(x, y, player.z);
        caption = String.format("%s - %s", tile.glyph(), tile.details());
    }
}