package engine;

import java.util.ArrayList;
import java.util.List;

public class LevelUpController {

	private static LevelUpOption[] options = new LevelUpOption[]{
		new LevelUpOption("Increase Health"){
			public void invoke(Creature creature) { creature.gainMaxHp(10); }
		},
		new LevelUpOption("Increase HP Regen"){
			public void invoke(Creature creature) {creature.gainHpRegen(1); }
		},
		new LevelUpOption("Increase Attack"){
			public void invoke(Creature creature) { creature.gainAttackValue(2); }
		},
		new LevelUpOption("Increase Defense"){
			public void invoke(Creature creature) { creature.gainDefenseValue(2); }
		},
		new LevelUpOption("Increase Vision"){
			public void invoke(Creature creature) { creature.gainVision(1); }
		},
		new LevelUpOption("Increase Max Hunger"){
			public void invoke(Creature creature) { creature.gainHunger(); }
		},
		new LevelUpOption("Increase Mining Ability"){
			public void invoke(Creature creature) { creature.gainMiningLevel(); }
		},
	};
	
	public void autoLevelUp(Creature creature){
		options[(int)(Math.random() * options.length)].invoke(creature);
	}
	
	public List<String> getLevelUpOptions(){
		List<String> names = new ArrayList<String>();
		for (LevelUpOption option : options){
			names.add(option.name());
		}
		return names;
	}
	
	public LevelUpOption getLevelUpOption(String name){
		for (LevelUpOption option : options){
			if (option.name().equals(name))
				return option;
		}
		return null;
	}
}
