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
	private int mapWidth = Config.MAP_W;
	private int mapHeight = Config.MAP_H;
	private List<String> messages;
	private FieldOfView fov;
	private Screen subscreen;
	
	public PlayScreen(){
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
			for (int i = 0; i < z * 5; i++){
				factory.newFungus(z);
			}
			for (int i = 0; i < 40; i++){
				factory.newBat(z);
			}
			for (int i = 0; i < z * 10; i++){
				factory.newZombie(z, player);
			}
			for (int i = 0; i < z * 5; i++){
				factory.newGoblin(z, player);
			}
			for (int i = 0; i < z * 1 ; i++){
				factory.newTroll(z, player);
			}			
		}
	}

	private void createItems(StuffFactory factory) {
		for (int z = 0; z < world.depth(); z++){
			for (int i = 0; i < world.width() * world.height() / 8; i++){
				factory.newRock(z);
			}
			for (int i = 0; i < 20; i++){
				factory.randomArmor(z);
				factory.randomWeapon(z);
				factory.randomPotion(z);
			}
			for (int i = 0; i < 60; i++){
				factory.randomFood(z);
			}
		}

		factory.newDragon(world.depth() - 1, player);
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

		
		
		String stats = String.format("%s/%s HP - %s - Dungeon: %s - Turn: %s - %s/%s EXP",
										player.hp(), player.maxHp(), hunger(), player.z + 1, player.numberOfTurns() + 1, player.xp(), player.xpToNextLevel());
		terminal.write(stats, mapWidth + 2, 1);
		terminal.write("- Message Log - ", mapWidth + 2, 3);
		for (int i = 0; i < mapHeight + 1; i++) {
			terminal.write((char)177, mapWidth, separatorY++);
		}
		for (int i = 24; i < 36; i++) {
			terminal.write('|', mapWidth, separatorY++);
		}

		for (int i = 0; i < mapWidth; i++) {
			terminal.write((char)177, separatorX++, mapHeight);
		}
		
		if (subscreen != null)
			subscreen.displayOutput(terminal);
	}
	
	private String hunger(){
		return "Food: "+player.food();
	}

	private void displayMessages(AsciiPanel terminal, List<String> messages) {

		for (int i = 0; i < messages.size(); i++) {
			terminal.write(messages.get(i), mapWidth + 2, i + 6);
		}
			
		if (messages.size() >= 22) {	
			messages.clear();
		}
	}


	private void displayTiles(AsciiPanel terminal) {
		fov.update(player.x, player.y, player.z, player.visionRadius());
		
		for (int x = 0; x < mapWidth; x++){
			for (int y = 0; y < mapHeight; y++){
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
			case KeyEvent.VK_HOME: return new StartScreen();
			case KeyEvent.VK_END: System.exit(0);
			
			case KeyEvent.VK_H: 
			case KeyEvent.VK_LEFT: player.moveBy(-1, 0, 0); break;


			case KeyEvent.VK_L:
			case KeyEvent.VK_RIGHT: player.moveBy( 1, 0, 0); break;

			case KeyEvent.VK_J:
			case KeyEvent.VK_UP: player.moveBy( 0, -1, 0); break;

			case KeyEvent.VK_K:
			case KeyEvent.VK_DOWN: player.moveBy( 0, 1, 0); break;

			// Diagonal movement
			case KeyEvent.VK_U: player.moveBy(-1, -1, 0); break;
			case KeyEvent.VK_I: player.moveBy( 1, -1, 0); break;
			case KeyEvent.VK_N: player.moveBy( -1, 1, 0); break;
			case KeyEvent.VK_M: player.moveBy( 1, 1, 0); break;

			case KeyEvent.VK_R: player.rest(); break;

			case KeyEvent.VK_D: subscreen = new DropScreen(player); break;
			case KeyEvent.VK_E: subscreen = new EatScreen(player); break;
			case KeyEvent.VK_W: subscreen = new EquipScreen(player); break;

			case KeyEvent.VK_V: subscreen = new MessageLogScreen(player, player.allMessages()); break;
			case KeyEvent.VK_C: subscreen = new CharacterScreen(player); break;
			case KeyEvent.VK_X: subscreen = new ExamineScreen(player); break;
			case KeyEvent.VK_T: subscreen = new ThrowScreen(player, 
											player.x, player.y); break;
			case KeyEvent.VK_BACK_SLASH: subscreen = new InventoryScreen(player); break;
			case KeyEvent.VK_Q: subscreen = new QuaffScreen(player); break;
			case KeyEvent.VK_F:
				if (player.rangedWeapon() == null || player.rangedWeapon().rangedAttackValue() == 0)
					player.notify("You don't have a ranged weapon equipped.");
				else
					subscreen = new FireWeaponScreen(player, player.x, player.y); break;
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
			case '?': subscreen = new GameHelpMenu(player); break;
			}

		if (player.level() > level)
			subscreen = new LevelUpScreen(player, player.level() - level);
		
		

		if (player.food() < 1)
			player.modifyHP(-30, "Starved");

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
