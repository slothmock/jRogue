package engine.screens;

import java.awt.event.KeyEvent;
import java.util.List;


import asciiPanel.AsciiPanel;
import engine.Creature;

public class MessageLogScreen implements Screen {

    protected Creature player;
    protected List<String> messages;
    protected AsciiPanel terminal;
    protected int y = 2;
    protected int messageIndex = 0;

    public MessageLogScreen(Creature player, List<String> messages) {
        this.player = player;
        this.messages = messages;
    }
    

    @Override
    public void displayOutput(AsciiPanel terminal) {
        int screenY = terminal.getHeightInCharacters();
        int screenX = terminal.getWidthInCharacters();
        terminal.clear();

        terminal.writeCenter("- Message History - [ESC] to close menu", y);
        for (int y = 0; y < screenY; y++) {
            terminal.write((char)177, screenX / 4, y);
            terminal.write((char)177, screenX - 35, y);
        }
        for (int i = 0; i < messages.size(); i++) {
            if (i > 28) {
                messages.clear();
            } else {
                terminal.write(messages.get(i), screenX / 3, i + y + 2);
            }
        }
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_ESCAPE: return null;
            default: return this;
        }
    }
}
