package engine;

import java.util.List;

import asciiPanel.AsciiPanel;

public class StuffFactory {
	private World world;
	
	public StuffFactory(World world){
		this.world = world;
	}

	public Creature newPlayer(List<String> messages, FieldOfView fov){
		Creature player = new Creature(world, '@', AsciiPanel.brightWhite, "You", 100, 15, 7);
		world.addAtEmptyLocation(player, 0);
		new PlayerAi(player, messages, fov);
		return player;
	}
	
	public Creature newFungus(int depth){
		Creature fungus = new Creature(world, 'f', AsciiPanel.green, "Fungus", 10, 0, 1);
		world.addAtEmptyLocation(fungus, depth);
		new FungusAi(fungus, this);
		return fungus;
	}
	
	public Creature newBat(int depth){
		Creature bat = new Creature(world, 'b', AsciiPanel.brightYellow, "Bat", 15, 3, 5);
		world.addAtEmptyLocation(bat, depth);
		new BatAi(bat);
		return bat;
	}
	
	public Creature newZombie(int depth, Creature player){
		Creature zombie = new Creature(world, 'z', AsciiPanel.white, "Zombie", 30, 10, 5);
		world.addAtEmptyLocation(zombie, depth);
		new ZombieAi(zombie, player);
		return zombie;
	}

	public Creature newGoblin(int depth, Creature player){
        Creature goblin = new Creature(world, 'g', AsciiPanel.brightGreen, "Goblin", 65, 12, 8);
        goblin.equip(randomWeapon(depth));
        goblin.equip(randomArmor(depth));
        world.addAtEmptyLocation(goblin, depth);
        new GoblinAi(goblin, player);
        return goblin;
    }

	public Creature newTroll(int depth, Creature player){
        Creature troll = new Creature(world, 'T', AsciiPanel.brightGreen, "Troll", 90, 30, 15);
        world.addAtEmptyLocation(troll, depth);
        new TrollAi(troll, player);
        return troll;
    }

	public Creature newDragon(int depth, Creature player){
		Creature dragon = new Creature(world, 'D', AsciiPanel.brightRed, "Dragon", 200, 50, 20);
		world.addAtEmptyLocation(dragon, depth);
		new DragonAi(dragon, player);
		return dragon;
	}
	
	public Item newRock(int depth){
		Item rock = new Item(',', AsciiPanel.yellow, "Rock");
		rock.modifyFoodValue(-200);
		rock.modifyThrownAttackValue(3);
		world.addAtEmptyLocation(rock, depth);
		return rock;
	}
	
	public Item newVictoryItem(int depth){
		Item item = new Item('*', AsciiPanel.brightWhite, "Cactus");
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newBread(int depth){
		Item item = new Item('%', AsciiPanel.yellow, "Loaf of Bread");
		item.modifyFoodValue(200);
		item.modifyThrownAttackValue(1);
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newOlive(int depth){
		Item item = new Item('%', AsciiPanel.green, "Olives");
		item.modifyFoodValue(30);
		item.modifyThrownAttackValue(1);
		world.addAtEmptyLocation(item, depth);
		return item;
	}

	public Item newApple(int depth){
		Item item = new Item('%', AsciiPanel.brightRed, "Apple");
		item.modifyFoodValue(50);
		item.modifyThrownAttackValue(2);
		world.addAtEmptyLocation(item, depth);
		return item;
	}

	public Item newBaguette(int depth){
		Item item = new Item(')', AsciiPanel.yellow, "Baguette");
		item.modifyAttackValue(3);
		item.modifyThrownAttackValue(2);
		item.modifyFoodValue(50);
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newDagger(int depth){
		Item item = new Item(')', AsciiPanel.white, "Dagger");
		item.modifyAttackValue(5);
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newSword(int depth){
		Item item = new Item(')', AsciiPanel.brightWhite, "Sword");
		item.modifyAttackValue(10);
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newStaff(int depth){
		Item item = new Item(')', AsciiPanel.yellow, "Staff");
		item.modifyAttackValue(5);
		item.modifyDefenseValue(3);
		world.addAtEmptyLocation(item, depth);
		return item;
	}

	public Item newBow(int depth){
        Item item = new Item(')', AsciiPanel.yellow, "Bow");
        item.modifyAttackValue(1);
        item.modifyRangedAttackValue(5);
        world.addAtEmptyLocation(item, depth);
        return item;
    }

	public Item newPotionOfHealth(int depth){
		Item item = new Item('!', AsciiPanel.red, "Health Potion");
		item.setQuaffEffect(new Effect(1){
			public void start(Creature creature){
				if (creature.hp() == creature.maxHp())
					return;
									
				creature.modifyHp(20 + creature.level() * 5, "Killed by - " + item.name());
				creature.notify("You drink the %s", item.name());
				creature.doAction("gain %d HP", 15);
			}
		});
		world.addAtEmptyLocation(item, depth);
		return item;
	}

	public Item newPotionOfPoison(int depth){
		Item item = new Item('!', AsciiPanel.white, "Poison Potion");
		item.setQuaffEffect(new Effect(5){
			public void start(Creature creature){
				creature.notify("You drink the %s", item.name());
				creature.doAction("look sick");
			}
							
			public void update(Creature creature){
				super.update(creature);
				creature.modifyHp(-3, "Killed by - " + item.quaffEffect());
			}

			public void end(Creature creature){
				creature.notify("You feel better");
			}
		});		
					
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newPotionOfWarrior(int depth){
		Item item = new Item('!', AsciiPanel.white, "Warrior's Potion");
		item.setQuaffEffect(new Effect(10) {

			public void start(Creature creature){
				creature.gainAttackValue(5);
				creature.gainDefenseValue(5);
				creature.notify("You drink the %s", item.name());
				creature.doAction("gain 5 attack and defense.");
			}
			public void end(Creature creature){
				creature.gainAttackValue(-5);
				creature.gainDefenseValue(-5);
				creature.notify("The potion wears off.");
				creature.doAction("have your usual stats again.");
			}
		});
					
		world.addAtEmptyLocation(item, depth);
		return item;
	}

	public Item newPotionOfSight(int depth) {
		Item item = new Item('!', AsciiPanel.white, "Sight Potion");
		item.setQuaffEffect(new Effect(10) {

			public void start(Creature creature){
				creature.gainVision(5);
				creature.notify("You drink the %s", item.name());
				creature.doAction("gain 5 sight.");
			}
			public void end(Creature creature){
				creature.gainVision(-5);
				creature.notify("The potion wears off.");
				creature.doAction("have your usual sight again.");
			}
		});
					
		world.addAtEmptyLocation(item, depth);
		return item;
	}

	public Item newBlinkPotion(int depth) {
		Item item = new Item('!', AsciiPanel.brightBlue, "Blink Potion");
		item.setQuaffEffect(new Effect(5) {

			public void start(Creature creature){
				creature.z += 1;
				creature.blinkBlindness();

				creature.notify("You blink. Stumble around for a while to find your way.");
				
			}
			public void update(Creature creature){
				super.update(creature);
			}
			public void end(Creature creature){
				creature.notify("The potion wears off.");
				creature.regainVision();
			}
		});


					
		world.addAtEmptyLocation(item, depth);
		return item;
	}

	public Item newPotionOfExperience(int depth){
		Item item = new Item('!', AsciiPanel.brightYellow, "Experience Potion");
		item.setQuaffEffect(new Effect(2) {

			public void start(Creature creature){
				creature.modifyXp(100);
				creature.doAction("gain 100 experience.");
			}
		});
					
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newTunic(int depth){
		Item item = new Item('[', AsciiPanel.green, "Tunic");
		item.modifyDefenseValue(2);
		world.addAtEmptyLocation(item, depth);
		return item;
	}

	public Item newLeatherArmor(int depth){
		Item item = new Item('[', AsciiPanel.yellow, "Leather Armor");
		item.modifyDefenseValue(3);
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newChainmail(int depth){
		Item item = new Item('[', AsciiPanel.white, "Chainmail");
		item.modifyDefenseValue(6);
		world.addAtEmptyLocation(item, depth);
		return item;
	}

	public Item newPlatemail(int depth){
		Item item = new Item('[', AsciiPanel.brightWhite, "Platemail");
		item.modifyDefenseValue(10);
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item randomWeapon(int depth){
		switch ((int)(Math.random() * 3)){
		case 0: return newDagger(depth);
		case 1: return newSword(depth);
		case 2: return newBow(depth);
		default: return newStaff(depth);
		}
	}

	public Item randomArmor(int depth){
		switch ((int)(Math.random() * 3)){
		case 0: return newTunic(depth);
		case 1: return newLeatherArmor(depth);
		case 2: return newChainmail(depth);
		default: return newPlatemail(depth);
		}
	}

	public Item randomPotion(int depth){
		switch ((int)(Math.random() * 5)){
		case 0: return newPotionOfWarrior(depth);
		case 1: return newPotionOfPoison(depth);
		case 2: return newPotionOfExperience(depth);
		case 3: return newPotionOfSight(depth);
		case 4: return newBlinkPotion(depth);
		default: return newPotionOfHealth(depth);
		}
}

	public Item randomFood(int depth){
		switch ((int)(Math.random() * 3)){
		case 0: return newApple(depth);
		case 1: return newOlive(depth);
		case 2: return newBaguette(depth);
		default: return newBread(depth);
		}
	}
}
