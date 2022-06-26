package engine;

public class TrollAi extends CreatureAi {
    private Creature player;

    public TrollAi(Creature creature, Creature player) {
        super(creature);
        this.player = player;
    }

    public void onUpdate() {
        if (creature.canSee(player.x, player.y, player.z))
            hunt(player);
        else
            wander();
    }

}
