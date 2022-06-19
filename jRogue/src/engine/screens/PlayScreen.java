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
	//TODO: Add message history
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
			for (int i = 0; i < 10; i++){
				factory.newFungus(z);
			}
			for (int i = 0; i < 25; i++){
				factory.newBat(z);
			}
			for (int i = 0; i < z + 4; i++){
				factory.newZombie(z, player);
			}
		}
	}

	private void createItems(StuffFactory factory) {
		for (int z = 0; z < world.depth(); z++){
			for (int i = 0; i < world.width() * world.height() / 5; i++){
				factory.newRock(z);
			}
			Item item = new Item('%', AsciiPanel.brightRed, "Apple");
			item.modifyFoodValue(100);
			world.addAtEmptyLocation(item, z);

			factory.newEdibleWeapon(z);
			factory.newBread(z);
			factory.randomArmor(z);
			factory.randomWeapon(z);
			factory.randomWeapon(z);
		}
		factory.newVictoryItem(world.depth() - 1);
	}
	
	private void createWorld(){
		world = new WorldBuilder(80, 23, 10)
					.makeCaves()
					.build();
		messages.add(Config.INTRO_MSG);
		messages.add(Config.HELP_MSG);
	}


	
	
	@Override
	public void displayOutput(AsciiPanel terminal) {
		int separatorY = 0; 
		int separatorX = 0;
		
		displayTiles(terminal);
		displayMessages(terminal, messages);

		
		
		String stats = String.format("%s/%s HP - %s - %s XP - Dungeon Level: %s", 
									player.hp(), player.maxHp(), hunger(), player.xp(), player.z + 1);
		terminal.write(stats, screenWidth + 2, 1);
		terminal.write("- Message Log - ", screenWidth + 2, 3);
		for (int i = 0; i < screenHeight + 1; i++) {
			terminal.write((char)177, screenWidth, separatorY++);
		}
		for (int i = 24; i < 36; i++) {
			terminal.write('|', screenWidth, separatorY++);
		}

		for (int i = 0; i < screenWidth; i++) {
			terminal.write((char)177, separatorX++, screenHeight);
		}
		
		if (subscreen != null)
			subscreen.displayOutput(terminal);
	}
	
	private String hunger(){
		if (player.food() <= player.maxFood() * 0.3)
			return "Starving";

		else if (player.food() > player.maxFood() * 0.8)
			return "Full";

		else if (player.food() > player.maxFood() * 0.6)
			return "Peckish";

		else if (player.food() > player.maxFood() * 0.3)
			return "Hungry";

		else
			return "";
	}

	private void displayMessages(AsciiPanel terminal, List<String> messages) {
		int top = screenHeight - messages.size() + 10;

		for (int i = 0; i < messages.size(); i++) {
			terminal.write(messages.get(i), screenWidth + 2, top + i);
		}
			
		if (messages.size() >= 26) {	
			messages.clear();
		}
	}


	private void displayTiles(AsciiPanel terminal) {
		fov.update(player.x, player.y, player.z, player.visionRadius());
		
		for (int x = 0; x < screenWidth; x++){
			for (int y = 0; y < screenHeight; y++){
				int wx = x;
				int wy = y;

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
			case KeyEvent.VK_HOME:
			case KeyEvent.VK_END: System.exit(0);
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
			case KeyEvent.VK_X: subscreen = new ExamineScreen(player); break;
			case KeyEvent.VK_SLASH: subscreen = new LookScreen(
				player, "Looking",	player.x, player.y); break;
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
			case '?': subscreen = new GameHelpScreen(player); break;
			}

		if (player.level() > level)
			subscreen = new LevelUpScreen(player, player.level() - level);
		
		if (player.food() < 1)
			return new HungerDeathScreen();

		if (player.hp() < 1)
			return new DiedScreen();

		if (subscreen == null)
			world.update();
		
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
		return new NoCactusScreen();
	}
}
