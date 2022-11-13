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
        "[H-J-K-L-U-I-N-M]",
        "Shift + < [UPSTAIRS] or Shift + > [DOWNSTAIRS]",
        "[G]rab from the ground",
        "[X]amine an item from your inventory",
        "[E]at an item",
        "[W]ield/Wear an item",
        "[F]ire weapon (if equipped)",
        "[D]rop an item",
        "[T]hrow an item",
        "[Q]uaff an item (Drink)",
        "[R]est (+HP/-Food)",
        "",
        "[?] - Help Menu",
        "[C]haracter Information",
        "[/] - Look Around",
        "[\\] View Inventory",
        "[V]iew message log",
        "Press [Home] to go back to the main menu",
        "[End] to quit the game",
        "",
        "Press [Esc] to close menu",
    };
}
