package engine;

import java.awt.Color;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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

	private int randomXPGain;
	public int randomXPGain() { 
		randomXPGain = ThreadLocalRandom.current().nextInt(1, 4);
		return randomXPGain;
	}

	
	public Creature(World world, char glyph, Color color, String name, int maxHp, int attack, int defense){
		this.world = world;
		this.glyph = glyph;
		this.color = color;
		this.maxHp = maxHp;
		this.hp = maxHp;
		this.attackValue = attack;
		this.defenseValue = defense;
		this.visionRadius = 3;
		this.name = name;
		this.inventory = new Inventory(12);
		this.maxFood = 1000;
		this.food = maxFood;
		this.level = 1;
	}
	
	public void moveBy(int mx, int my, int mz){

		if (mx==0 && my==0 && mz==0)
			return;
		
		Tile tile = world.tile(x+mx, y+my, z+mz);
		
		if (mz == -1){
			if (tile == Tile.STAIRS_DOWN) {
				doAction("go up the stairs - level %d", z+mz+1);
			} else {
				doAction("try to go up but are stopped by the cave ceiling");
				return;
			}
		} else if (mz == 1){
			if (tile == Tile.STAIRS_UP) {
				doAction("go down the stairs - level %d", z+mz+1);
			} else {
				doAction("try to go down but are stopped by the cave floor");
				return;
			}
		}
		
		Creature other = world.creature(x+mx, y+my, z+mz);
		
		if (other == null)
			ai.onEnter(x+mx, y+my, z+mz, tile);
		else
			attack(other);

	}

	public void attack(Creature other){
		modifyFood(-3);
		
		int amount = Math.max(0, attackValue() - other.defenseValue());
		
		amount = (int)(Math.random() * amount) + 1;
		
		doAction("attack %s for %d damage", other.name, amount);
		
		other.modifyHp(-amount);
		
		if (other.hp < 1)
			gainXp(other);
	}

	public void modifyXp(int amount) { 
		xp += amount;
		
		notify("You %s %d xp.", amount < 0 ? "lose" : "gain", amount);

		levelCheck();
	}

	private void levelCheck() {
		while (xp > (int)(Math.pow(level, 1.5) * 20)) {
			level++;
			doAction("advance to level %d", level);
			ai.onGainLevel();
			modifyHp(level * 2);
		}
	}
	
	public void gainXp(Creature other){
		int amount = other.maxHp 
			+ other.attackValue() 
			+ other.defenseValue()
			- level;
		
		if (amount > 0)
			modifyXp(amount);
	}

	public void modifyHp(int amount) { 
		hp += amount;
		
		if (hp > maxHp) {
			hp = maxHp;
		} else if (hp < 1) {
			doAction("die");
			leaveCorpse();
			world.remove(this);
		}
	}
	
	private void leaveCorpse(){
		Item corpse = new Item('%', color, name + " remains");
		corpse.modifyFoodValue(maxHp);
		world.addAtEmptySpace(corpse, x, y, z);
	}
	
	public void dig(int wx, int wy, int wz) {
		modifyFood(-10);
		world.dig(wx, wy, wz);
		doAction("dig");
		modifyXp(randomXPGain());
	}
	
	public void update(){
		modifyFood(-1);
		ai.onUpdate();

	}


	public boolean canEnter(int wx, int wy, int wz) {
		return world.tile(wx, wy, wz).isGround() && world.creature(wx, wy, wz) == null;
	}

	public void notify(String message, Object ... params){
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
		return world.tile(wx, wy, wz);
	}

	public Creature creature(int wx, int wy, int wz) {
		return world.creature(wx, wy, wz);
	}
	
	public void pickup(){
		Item item = world.item(x, y, z);
		
		if (item == null){
			doAction("grab at the ground");
		} else if (inventory.isFull()) {
			notify("Can't pick up %s because inventory is full", item.name());
		} else {
			doAction("picked up - %s", item.name());
			world.remove(x, y, z);
			inventory.add(item);
		}
	}
	
	public void drop(Item item){
		if (world.addAtEmptySpace(item, x, y, z)){
			doAction("dropped - " + item.name());
			inventory.remove(item);
			unequip(item);
		} else {
			notify("There's nowhere to drop - %s.", item.name());
		}
	}
	
	public void modifyFood(int amount) { 
		food += amount;
		
		if (food > maxFood) {
			maxFood = (maxFood + food) / 2;
			food = maxFood;
			notify("You can't believe your stomach can hold that much!");
			modifyHp(-1);
		} else if (food < 1 && isPlayer()) {
			modifyHp(-1000);
		}
	}
	
	public boolean isPlayer(){
		return glyph == '@';
	}
	
	public void eatFood(Item item){
		if (item.name() == "Rock") {
			notify("You break your tooth eating the rock.");
			modifyHp(-2);
			inventory.remove(item);
		} else if (item.foodValue() < 25) {
			modifyFood(item.foodValue());
			notify("You eat the %s", item.name());
			notify("It's not very good.");
			inventory.remove(item);
			unequip(item);
		} else if (item.name().contains("remains") && (!item.name().contains("fungus"))) {
			modifyFood(item.foodValue());
			notify("You eat the %s", item.name());
			notify("It's disgusting!");
			inventory.remove(item);
			unequip(item);
		} else {
			modifyFood(item.foodValue());
			notify("You eat the %s.", item.name());
			inventory.remove(item);
			unequip(item);
		}
	}
	
	public void unequip(Item item){
		if (item == null)
			return;
		
		if (item == armor){
			doAction("Removed - " + item.name());
			armor = null;
		} else if (item == weapon) {
			doAction("Put away - " + item.name());
			weapon = null;
		}
	}
	
	public void equip(Item item){
		if (item.attackValue() == 0 && item.defenseValue() == 0)
			return;
		
			if (item.attackValue() >= item.defenseValue()){
				if (weapon != null) {
					unequip(weapon);
				} else { 
					doAction("Wield a - " + item.name());
					weapon = item;
			}
				
			} else {
				if (armor != null) {unequip(armor);
				} else {
					doAction("Put on - " + item.name());
					armor = item;
				}
			}
		}
	
	public void gainMaxHp() {
		maxHp += 10;
		hp = maxHp;
		doAction("look healthier");
	}
	
	public void gainAttackValue() {
		attackValue += 2;
		doAction("are stronger");
	}
	
	public void gainDefenseValue() {
		defenseValue += 2;
		doAction("are tougher");
	}
	
	public void gainVision() {
		visionRadius += 1;
		doAction("are more aware");
	}

	public void gainHunger() {
		maxFood += 100;
		food = maxFood;
		doAction("get hungry less often");
	}
}
