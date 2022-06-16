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
		
		int x = 1;
		int y = 25;
		terminal.clear(' ', x, y, 40, options.size() + 2);
		terminal.write("   Choose a level up bonus    ", x, y++);
		terminal.write("------------------------------", x, y++);
		
		for (int i = 0; i < options.size(); i++){
			terminal.write(String.format("[%d] %s", i+1, options.get(i)), x, y++);
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
