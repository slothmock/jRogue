package engine;

public class DragonAi extends CreatureAi {

    protected Creature player;
    
    public DragonAi(Creature creature, Creature player) {
        super(creature);
        this.player = player;
        creature.gainVision(7);
    }

    public void onUpdate() {
        if (creature.canSee(player.x, player.y, player.z)) {
            hunt(player);
            hunt(player);
        } else {
            wander();
            wander();
        }
    }
}
