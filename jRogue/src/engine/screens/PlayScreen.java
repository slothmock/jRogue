package engine.screens;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import asciiPanel.AsciiPanel;
import engine.Config;
import engine.Creature;
import engine.FieldOfView;
import engine.Item;
import engine.StuffFactory;
import engine.Tile;
import engine.World;
import engine.WorldBuilder;

public class PlayScreen implements Screen {
	private World world;
	private Creature player;
	private int screenWidth;
	private int screenHeight;
	private List<String> messages;
	private FieldOfView fov;
	private Screen subscreen;
	
	public PlayScreen(){
		screenWidth = Config.SCREEN_W;
		screenHeight = Config.SCREEN_H;
		messages = new ArrayList<String>();
		createWorld();
		fov = new FieldOfView(world);
		
		StuffFactory factory = new StuffFactory(world);
		createCreatures(factory);
		createItems(factory);
	}

	private void createCreatures(StuffFactory factory){
		player = factory.newPlayer(messages, fov);
		
		for (int z = 0; z < world.depth(); z++){
			for (int i = 0; i < 8; i++){
				factory.newFungus(z);
			}
			for (int i = 0; i < 20; i++){
				factory.newBat(z);
			}
			for (int i = 0; i < z + 3; i++){
				factory.newZombie(z, player);
			}
		}
	}

	private void createItems(StuffFactory factory) {
		for (int z = 0; z < world.depth(); z++){
			for (int i = 0; i < world.width() * world.height() / 20; i++){
				factory.newRock(z);
			}

			factory.newFruit(z);
			factory.newEdibleWeapon(z);
			factory.newBread(z);
			factory.randomArmor(z);
			factory.randomWeapon(z);
			factory.randomWeapon(z);
		}
		factory.newVictoryItem(world.depth() - 1);
	}
	
	private void createWorld(){
		world = new WorldBuilder(87, 32, 25)
					.makeCaves()
					.build();
	}
	
	public int getScrollX() { return Math.max(0, Math.min(player.x - screenWidth / 2, world.width() - screenWidth)); }
	
	public int getScrollY() { return Math.max(0, Math.min(player.y - screenHeight / 2, world.height() - screenHeight)); }
	
	@Override
	public void displayOutput(AsciiPanel terminal) {
		int left = getScrollX();
		int top = getScrollY(); 
		
		displayTiles(terminal, left, top);
		displayMessages(terminal, messages);
		
		String stats = String.format("%s/%s HP - %s %s - %s XP", player.hp(), player.maxHp(), player.food(), hunger(), player.xp());
		terminal.write(stats, 88, 1);
		
		if (subscreen != null)
			subscreen.displayOutput(terminal);
	}
	
	private String hunger(){
		if (player.food() < player.maxFood() * 0.2)
			return "(Starving)";
		else if (player.food() < player.maxFood() * 0.5)
			return "(Hungry)";
		else if (player.food() > player.maxFood() * 0.5)
			return "(Not Hungry)";
		else if (player.food() > player.maxFood() * 0.8)
			return "(Full)";
		else if (player.food() > player.maxFood() * 0.95)
			return "(Stuffed)";
		else
			return "";
	}

	private void displayMessages(AsciiPanel terminal, List<String> messages) {
		int top = screenHeight - messages.size();
		for (int i = 0; i < messages.size(); i++){
			terminal.write(messages.get(i), 88, top + i);
		}
		if (messages.size() > 8) messages.clear();
	}

	private void displayTiles(AsciiPanel terminal, int left, int top) {
		fov.update(player.x, player.y, player.z, player.visionRadius());
		
		for (int x = 0; x < screenWidth; x++){
			for (int y = 0; y < screenHeight; y++){
				int wx = x + left;
				int wy = y + top;

				if (player.canSee(wx, wy, player.z))
					terminal.write(world.glyph(wx, wy, player.z), x, y, world.color(wx, wy, player.z));
				else
					terminal.write(fov.tile(wx, wy, player.z).glyph(), x, y, Color.darkGray);
			}
		}
	}
	
	@Override
	public Screen respondToUserInput(KeyEvent key) {
		int level = player.level();
		if (subscreen != null) {
			subscreen = subscreen.respondToUserInput(key);
		} else switch (key.getKeyCode()) {
			case KeyEvent.VK_ESCAPE: System.exit(0);
			case KeyEvent.VK_LEFT: player.moveBy(-1, 0, 0); break;
			case KeyEvent.VK_RIGHT: player.moveBy( 1, 0, 0); break;
			case KeyEvent.VK_UP: player.moveBy( 0,-1, 0); break;
			case KeyEvent.VK_DOWN: player.moveBy( 0, 1, 0); break;
			case KeyEvent.VK_Y: player.moveBy(-1,-1, 0); break;
			case KeyEvent.VK_U: player.moveBy( 1,-1, 0); break;
			case KeyEvent.VK_B: player.moveBy(-1, 1, 0); break;
			case KeyEvent.VK_N: player.moveBy( 1, 1, 0); break;
			case KeyEvent.VK_D: subscreen = new DropScreen(player); break;
			case KeyEvent.VK_E: subscreen = new EatScreen(player); break;
			case KeyEvent.VK_W: subscreen = new EquipScreen(player); break;
			case KeyEvent.VK_H: subscreen = new GameHelpScreen(player); break;
			case KeyEvent.VK_C: subscreen = new CharacterScreen(player); break;
			}
			
			switch (key.getKeyChar()){
			case 'g':
			case ',': player.pickup(); break;
			case '<': 
				if (userIsTryingToExit())
					return userExits();
				else
					player.moveBy( 0, 0, -1); break;
			case '>': player.moveBy( 0, 0, 1); break;
			}

		if (player.level() > level)
			subscreen = new LevelUpScreen(player, player.level() - level);
		
		if (subscreen == null)
			world.update();
		
		if (player.hp() < 1)
			return new LoseScreen();
		
		return this;
	}

	private boolean userIsTryingToExit(){
		return player.z == 0 && world.tile(player.x, player.y, player.z) == Tile.STAIRS_UP;
	}
	
	private Screen userExits(){
		for (Item item : player.inventory().getItems()){
			if (item != null && item.name().equals("Cactus"))
				return new WinScreen();
		}
		return new LoseScreen();
	}
}
