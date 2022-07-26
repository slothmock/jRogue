package engine;


public class Config {
    public final static String TITLE = "jRogue";
    public final static int MAP_H = 23;
    public final static int MAP_W = 80;
    public final static String INTRO_MSG = "Zak: What a crazy trip! Wait... Where am I!?";
    public final static String HELP_MSG = "[Press [?] for help]";
    public final static String[] HELP_MENU_MSGLIST = {
        "-- Help Menu --",
        "",
        "Arrow Keys + Classic Roguelike controls to move around",
        "[?] - Help Menu",
        "[/] - Look Around",
        "Shift + < or Shift + > to use stairs",
        "[G]rab from the ground",
        "[E]at an item",
        "[W]ield/Wear an item",
        "[R]est (+HP/-Food)",
        "Press [Home] to go back to the main menu",
        "[End] to quit the game",
        "",
        "[C]haracter Information",
        "[D]rop an item",
        "[T]hrow an item",
        "[Q]uaff an item (Drink)",
        "[I]nventory",
        "[V]iew message log",
        "[X]amine an item from your inventory",
        "",
        "Press [Esc] to close menu",};

    public final static String hitFXPath = "jRogue/src/engine/assets/sound/hit.wav";
}
