package engine;

import java.awt.Color;

public class Item {

	private char glyph;
	public char glyph() { return glyph; }
	
	private Color color;
	public Color color() { return color; }

	private String name;
	public String name() { return name; }
	
	private int foodValue;
	public int foodValue() { return foodValue; }
	public void modifyFoodValue(int amount) { foodValue += amount; }

	private int attackValue;
	public int attackValue() { return attackValue; }
	public void modifyAttackValue(int amount) { attackValue += amount; }

	private int defenseValue;
	public int defenseValue() { return defenseValue; }
	public void modifyDefenseValue(int amount) { defenseValue += amount; }

	private int durabilityValue;
	public int durabilityValue() { return durabilityValue; }
	public void modifydurabilityValue(int amount) { durabilityValue += amount;}

	private int thrownAttackValue;
    public int thrownAttackValue() { return thrownAttackValue; }
    public void modifyThrownAttackValue(int amount) { thrownAttackValue += amount; }

	private int rangedAttackValue;
    public int rangedAttackValue() { return rangedAttackValue; }
    public void modifyRangedAttackValue(int amount) { rangedAttackValue += amount; }

	private Effect quaffEffect;
	public Effect quaffEffect() { return quaffEffect; }
	public void setQuaffEffect(Effect effect) { this.quaffEffect = effect; }

	
	public Item(char glyph, Color color, String name){
		this.glyph = glyph;
		this.color = color;
		this.name = name;
		this.thrownAttackValue = 1;
	}

	public String details() {
		String details = "";
		
		if (attackValue > 0)
			details += " Damage: " + attackValue;

		if (thrownAttackValue > 0)
			details += " Thrown DMG: " + thrownAttackValue;
		
		if (rangedAttackValue > 0)
			details += " Ranged DMG: " + rangedAttackValue;

		if (durabilityValue > 0)
			details += " Durability: " + durabilityValue;
		
		if (defenseValue > 0)
			details += " Defense: " + defenseValue;

		if (foodValue > 0)
			details += " Food: " + foodValue;
		
		return details;
	}
}
