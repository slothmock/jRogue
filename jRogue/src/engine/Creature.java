package engine;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;


public class Creature {
	private World world;
	
	public int x;
	public int y;
	public int z;
	
	private char glyph;
	public char glyph() { return glyph; }
	
	private Color color;
	public Color color() { return color; }

	private CreatureAi ai;
	public void setCreatureAi(CreatureAi ai) { this.ai = ai; }
	
	private int maxHp;
	public int maxHp() { return maxHp; }
	
	private int hp;
	public int hp() { return hp; }

	private List<Effect> effects;
	public List<Effect> effects(){ return effects; }

	private int regenHpCooldown = 200;
    private int regenHpAmount;
	public int regenHpAmount() { return regenHpAmount; }
    public void modifyRegenHp(int amount) { regenHpAmount += amount; }
	
	private int attackValue;
	public int attackValue() { 
		return attackValue
			+ (weapon == null ? 0 : weapon.attackValue())
			+ (armor == null ? 0 : armor.attackValue());
	}

	private int defenseValue;
	public int defenseValue() { 
		return defenseValue
			+ (weapon == null ? 0 : weapon.defenseValue())
			+ (armor == null ? 0 : armor.defenseValue());
	}

	private int visionRadius;
	public int visionRadius() { return visionRadius; }

	private int miningLevel;
	public int miningLevel() { return miningLevel; }

	private String name;
	public String name() { return name; }

	private Inventory inventory;
	public Inventory inventory() { return inventory; }

	private int maxFood;
	public int maxFood() { return maxFood; }
	
	private int food;
	public int food() { return food; }
	
	private int foodValue;
	public int foodValue() { return foodValue; }

	private Item weapon;
	public Item weapon() { return weapon; }
	
	private Item armor;
	public Item armor() { return armor; }

	private int level;
	public int level() { return level; }
	
	private int xp;
	public int xp() { return xp; }
	
	public int xpToNextLevel() { return (int)(Math.pow(level, 1.6) * 30); }

	private int miningXPGain;
	public int miningXPGain() { 
		miningXPGain = (int)Math.random() * (level * 2) + 1;
		miningXPGain += miningLevel;  
		return miningXPGain;
	}

	public String causeOfDeath;
 	public String causeOfDeath() { return causeOfDeath; }

	private int numberOfTurns;
	public int numberOfTurns() { return numberOfTurns; }

	List<String> allMessages = new ArrayList<String>();
	public List<String> allMessages() { return allMessages; }

	
	public Creature(World world, char glyph, Color color, String name, int maxHp, int attack, int defense){
		this.world = world;
		this.glyph = glyph;
		this.color = color;
		this.maxHp = maxHp;
		this.hp = maxHp;
		this.attackValue = attack;
		this.defenseValue = defense;
		this.visionRadius = 3;
		this.miningLevel = 1;
		this.name = name;
		this.inventory = new Inventory(8);
		this.maxFood = 750;
		this.food = maxFood;
		this.level = 1;
		this.regenHpAmount = 3;
		this.effects = new ArrayList<Effect>();
		this.allMessages = allMessages();
	}
	
	public void moveBy(int mx, int my, int mz){

		if (mx==0 && my==0 && mz==0)
			return;
		
		Tile tile = world.tile(x+mx, y+my, z+mz);
		
		if (mz == -1){
			if (tile == Tile.STAIRS_DOWN) {
				doAction("go up the stairs - level %d", z+mz+1);
			} else {
				notify("Stopped by the cave ceiling");
				return;
			}
		} else if (mz == 1){
			if (tile == Tile.STAIRS_UP) {
				doAction("go down the stairs - level %d", z+mz+1);
			} else {
				notify("Stopped by the cave floor");
				return;
			}
		}
		
		Creature other = world.creature(x+mx, y+my, z+mz);
		
		if (other == null)
			ai.onEnter(x+mx, y+my, z+mz, tile);
		else
			meleeAttack(other);

	}

	public void meleeAttack(Creature other){
        commonAttack(other, attackValue(), "attack - %s for %d damage", other.name);
    }

    private void throwAttack(Item item, Creature other) {
        commonAttack(other, attackValue / 2 + item.thrownAttackValue(), "throw - %s at %s for %d damage", item.name(), other.name);
		other.addEffect(item.quaffEffect());
    }

    public void rangedWeaponAttack(Creature other){
        commonAttack(other, attackValue / 2 + weapon.rangedAttackValue(), "fire - %s at %s for %d damage", weapon.name(), other.name);
    }

    private void commonAttack(Creature other, int attack, String action, Object ... params) {
        modifyFood(-2);
    
        int amount = Math.max(0, attack - other.defenseValue());
    
        amount = (int)(Math.random() * amount) + 1;
    
        Object[] params2 = new Object[params.length+1];
        for (int i = 0; i < params.length; i++){
         params2[i] = params[i];
        }
        params2[params2.length - 1] = amount;
    
        doAction(action, params2);
		
        other.modifyHp(-amount, "Killed by - " + name);
		notify("[%s HP: %d]", other.name, Math.max(other.hp, 0));

        if (other.hp < 1)
            gainXp(other);
    }

	public void throwItem(Item item, int wx, int wy, int wz) {
		Point end = new Point(x, y, 0);
		
		for (Point p : new Line(x, y, wx, wy)){
			if (!realTile(p.x, p.y, z).isGround())
				break;
			end = p;
		}
		
		wx = end.x;
		wy = end.y;
		
		Creature c = creature(wx, wy, wz);
		

		if (c != null)
			throwAttack(item, c);				
		else
			doAction("throw - %s", item.name());
		
		if (item.quaffEffect() != null && c != null)
			getRidOf(item);
		else
			putAt(item, wx, wy, wz);
	}

	public void modifyXp(int amount) { 
		xp += amount;
		
		notify("You %s %d xp.", amount < 0 ? "lose" : "gain", amount);

		levelCheck();
	}

	private void levelCheck() {
		while (xp > xpToNextLevel()) {
			level++;
			doAction("advance to level %d", level);
			ai.onGainLevel();
			modifyHp(level * 4, "gained level");
		}
	}
	
	public void gainXp(Creature other){
		int amount = other.maxHp 
			+ other.attackValue() 
			+ other.defenseValue()
			- (int)(level * 1.5);
		
		if (amount > 0)
			modifyXp(amount);
	}

	public void modifyHp(int amount, String causeOfDeath) { 
     hp += amount;
     this.causeOfDeath = causeOfDeath;
  
     if (hp > maxHp) {
         hp = maxHp;
     } else if (hp < 1) {
         doAction("die");
         leaveCorpse();
         world.remove(this);
     }
 }

	private void regenerateHealth(){
        regenHpCooldown -= regenHpAmount;
        if (regenHpCooldown < 0){
			doAction("regenerate some health - %d hp", regenHpAmount);
            modifyHp(this.regenHpAmount, "regenerated");
            modifyFood(-1);
            regenHpCooldown += 120;
        }
    }
	
	private void leaveCorpse(){
        Item corpse = new Item('%', color, name + " corpse");
        corpse.modifyFoodValue(maxHp);

		switch (corpse.name())
		{
			case "Fungus":
				corpse.modifyThrownAttackValue(1); break;
			case "Bat":
				corpse.modifyThrownAttackValue(1); break;
			case "Zombie":
				corpse.modifyThrownAttackValue(2); break;
			case "Goblin":
				corpse.modifyThrownAttackValue(2); break;
			case "Troll":
				corpse.modifyThrownAttackValue(4); break;
			default:
				break;
		}
		corpse.modifyThrownAttackValue(1);
        world.addAtEmptySpace(corpse, x, y, z);
        for (Item item : inventory.getItems()){
            if (item != null)
				unequip(item);
                drop(item);
            }
    }

	private void getRidOf(Item item){
        inventory.remove(item);
        unequip(item);
    }

    private void putAt(Item item, int wx, int wy, int wz){
        inventory.remove(item);
        unequip(item);
        world.addAtEmptySpace(item, wx, wy, wz);
    }
	
	public void dig(int wx, int wy, int wz) {
		modifyFood(-10);
		world.dig(wx, wy, wz);
		doAction("dig");
		modifyXp(miningXPGain());
	}
	
	public void update(){
		modifyFood(-1);
		regenerateHealth();
		updateEffects();
		ai.onUpdate();
		numberOfTurns++;

	}


	public boolean canEnter(int wx, int wy, int wz) {
		return world.tile(wx, wy, wz).isGround() && world.creature(wx, wy, wz) == null;
	}

	public void notify(String message, Object ... params){
		allMessages.add(String.format(message, params));
		ai.onNotify(String.format(message, params));
	}
	
	public void doAction(String message, Object ... params){
		int r = 9;
		for (int ox = -r; ox < r+1; ox++){
			for (int oy = -r; oy < r+1; oy++){
				if (ox*ox + oy*oy > r*r)
					continue;
				
				Creature other = world.creature(x+ox, y+oy, z);
				
				if (other == null)
					continue;
				
				if (other == this)
					other.notify("You " + message + ".", params);
				else if (other.canSee(x, y, z))
					other.notify(String.format("%s %s.", name, makeSecondPerson(message)), params);
			}
		}
	}
	
	private String makeSecondPerson(String text){
		String[] words = text.split(" ");
		words[0] = words[0] + "s";
		
		StringBuilder builder = new StringBuilder();
		for (String word : words){
			builder.append(" ");
			builder.append(word);
		}
		
		return builder.toString().trim();
	}
	
	public boolean canSee(int wx, int wy, int wz){
		return ai.canSee(wx, wy, wz);
	}

	public Tile tile(int wx, int wy, int wz) {
        if (canSee(wx, wy, wz))
            return world.tile(wx, wy, wz);
        else
            return ai.rememberedTile(wx, wy, wz);
    }

	public Creature creature(int wx, int wy, int wz) {
        if (canSee(wx, wy, wz))
            return world.creature(wx, wy, wz);
        else
            return null;
    }
	
	public void pickup(){
		Item item = world.item(x, y, z);
		
		if (item == null){
			doAction("grab at the ground");
		} else if (inventory.isFull()) {
			notify("Can't pick up - %s - Inventory is full", item.name());
		} else {
			doAction("pick up - %s", item.name());
			world.remove(x, y, z);
			inventory.add(item);
		}
	}
	
	public void 
	drop(Item item){
		if (item == null)
			return;

		if (world.addAtEmptySpace(item, x, y, z)){
			doAction("drop - " + item.name());
			inventory.remove(item);
			unequip(item);
		} else {
			notify("There's nowhere to drop - %s.", item.name());
		}
	}

	public void quaff(Item item){
		doAction("quaff a " + item.name());
		consume(item);
	}

	public void eat(Item item){
		doAction("eat a " + item.name());
		consume(item);
	}

	public void rest() {
		int healAmount = (int) (int) (Math.random() * (5 - 1)) + 1;
		int foodAmount = (int) (int) (Math.random() * (25 - 5)) + 5;
		doAction("rest..");
		if (Math.random() * 100 > 80) {
			notify("You feel a little better. [+%d HP/-%d Food]", healAmount, foodAmount);
			modifyHp(healAmount, "rested");
			modifyFood(-foodAmount);
		} else {
			notify("It doesn't help. [+0 HP/-5 Food]");
			modifyFood(-5);
		}
	}
	
	private void consume(Item item){
		if (item.foodValue() < 0)
			notify("Gross!");
			
		addEffect(item.quaffEffect());
			
		modifyFood(item.foodValue());
		getRidOf(item);
	}
	
	private void addEffect(Effect effect){
		if (effect == null)
			return;
			
		effect.start(this);
		effects.add(effect);
	}

	private void updateEffects(){
		List<Effect> done = new ArrayList<Effect>();
			
		for (Effect effect : effects){
			effect.update(this);
			if (effect.isDone()) {
				effect.end(this);
				done.add(effect);
			}
		}
			
		effects.removeAll(done);
	}
	
	public void modifyFood(int amount) { 
		food += amount;
		
		if (food > maxFood) {
			maxFood = (maxFood + food) / 2;
			food = maxFood;
			notify("You can't believe your stomach can hold that much!");
			modifyHp(-1, "Killed by over-eating.");
		} else if (food < 1 && isPlayer()) {
			modifyHp(-1000, "Starved to death.");
		}
	}
	
	public boolean isPlayer(){
		return glyph == '@';
	}
	
	public void eatFood(Item item){
		if (item.name() == "Rock") {
			notify("You break your tooth eating the rock.");
			modifyHp(-2, "Ate a rock.");
			modifyFood(1);
			inventory.remove(item);
		} else if (item.foodValue() < 25) {
			modifyFood(item.foodValue());
			notify("Ate - %s", item.name());
			notify("It's not very good.");
			inventory.remove(item);
			unequip(item);
		} else if (item.name().contains("remains") && (!item.name().contains("fungus") )) {
			modifyFood(item.foodValue());
			notify("Ate - %s", item.name());
			notify("It's disgusting!");
			inventory.remove(item);
			unequip(item);
		} else {
			modifyFood(item.foodValue());
			notify("Ate - %s.", item.name());
			inventory.remove(item);
			unequip(item);
		}
	}
	
	public void unequip(Item item){
		if (item == null)
			return;
		
		if (item == armor){
			doAction("remove - " + item.name());
			armor = null;
		} else if (item == weapon) {
			doAction("unwield - " + item.name());
			weapon = null;
		}
	}
	
	public void equip(Item item){
        if (!inventory.contains(item)) {
            if (inventory.isFull()) {
                notify("Can't equip %s since you're holding too much stuff.", item.name());
                return;
            } else {
                world.remove(item);
                inventory.add(item);
            }
        }
    
        if (item.attackValue() == 0 && item.rangedAttackValue() == 0 && item.defenseValue() == 0)
            return;
    
        if (item.attackValue() + item.rangedAttackValue() >= item.defenseValue()){
            unequip(weapon);
            doAction("wield a " + item.name());
            weapon = item;
        } else {
            unequip(armor);
            doAction("put on a " + item.name());
            armor = item;
        }
    }
	
	public void gainMaxHp(int amount) {
		maxHp += amount;
		hp = maxHp;
		doAction("look healthier");
	}

	public void gainHpRegen(int amount) {
		regenHpAmount += amount;
		doAction("regen more health");
	}
	
	public void gainAttackValue(int amount) {
		attackValue += amount;
		doAction("are stronger");
	}
	
	public void gainDefenseValue(int amount) {
		defenseValue += amount;
		doAction("are tougher");
	}
	
	public void gainVision(int amount) {
		visionRadius += amount;
		doAction("are more aware");
	}

	public void gainMiningLevel() {
		miningLevel += 1;
		doAction("dig better");
	}

	public void gainHunger() {
		maxFood += 100;
		food = maxFood;
		doAction("get hungry less often");
	}

    public Tile realTile(int wx, int wy, int wz) {
        return world.tile(wx, wy, wz);
    }

	public Item item(int wx, int wy, int wz) {
        if (canSee(wx, wy, wz))
            return world.item(wx, wy, wz);
        else
            return null;
    }

	public String details() {
        return String.format("Level:%d - Attack:%d - Defense:%d - HP:%d", level, attackValue(), defenseValue(), hp);
    }
}
