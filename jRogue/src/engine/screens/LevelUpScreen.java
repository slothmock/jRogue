package engine.screens;

import java.awt.event.KeyEvent;
import java.util.List;


import engine.Creature;
import engine.LevelUpController;
import asciiPanel.AsciiPanel;

public class LevelUpScreen implements Screen {
	private LevelUpController controller;
	private Creature player;
	private int picks;
	
	public LevelUpScreen(Creature player, int picks){
		this.controller = new LevelUpController();
		this.player = player;
		this.picks = picks;
	}
	
	@Override
	public void displayOutput(AsciiPanel terminal) {
		List<String> options = controller.getLevelUpOptions();
		
		int x = 0;
		int x2 = 40;
		int y = 27; 
		
		terminal.clear(' ', x, y, 40, 9);
		terminal.write("- Choose your level up bonus -", 20, 25);
		
		for (int i = 0; i < options.size() - 3; i++){
			terminal.write(String.format("[%d] %s", i + 1, options.get(i)), x, y++);
		};
		y = 27;
		for (int i2 = 4; i2 < options.size(); i2++){
			terminal.write(String.format("[%d] %s", i2 + 1, options.get(i2)), x2, y++);
		}
	}

	@Override
	public Screen respondToUserInput(KeyEvent key) {
		List<String> options = controller.getLevelUpOptions();
		String chars = "";
		
		for (int i = 0; i < options.size(); i++){
			chars = chars + Integer.toString(i+1);
		}
		
		int i = chars.indexOf(key.getKeyChar());
		
		if (i < 0)
			return this;
		
		controller.getLevelUpOption(options.get(i)).invoke(player);
		
		if (--picks < 1)
			return null;
		else
			return this;
	}
}
