package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.CommandHandler;
import it.polimi.ingsw.exceptions.BadCommandException;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class Command implements Serializable {

    /**
     * Workers String representation used by Client to send commands, useful to avoid useless
     * Worker Object serialization. Also, we don't have reference to actual Workers on Client.
     *
     * Properties used by the Client to construct a PlayerCommand without the need to have the actual reference to Player and Worker instances.
     */
    public final static String WORKER_FIRST = "w1";
    public final static String WORKER_SECOND = "w2";

    protected final static String ROW_KEY = "row";
    protected final static String COL_KEY = "col";

    public final CommandType commandType;
    protected transient Player player;

    protected String playerNickname;
    protected String playerID;

    protected Command(CommandType commandType) {
        this.commandType = commandType;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public String getPlayerID() {
        return this.playerID;
    }

    public void setPlayerNickname(String playerNickname) {
        this.playerNickname = playerNickname;
    }

    public String getPlayerNickname() {
        return this.playerNickname;
    }

    public abstract void handleCommand(CommandHandler handler);

    protected static Map<String, Integer> parseCellIdentifiers(String partialCommand) {

        Map<String, Integer> identifiers = new HashMap<>();

        char rowChar = partialCommand.charAt(0);
        int colNum = Integer.parseInt(partialCommand.substring(1));

        if (partialCommand.length() != 2 || colNum < 1 || colNum > Board.WIDTH_SIZE || rowChar < 'a' || rowChar >= ('a' + Board.HEIGHT_SIZE)) {
            throw new BadCommandException();
        }

        identifiers.put(ROW_KEY, (int) rowChar - 'a');
        identifiers.put(COL_KEY, colNum - 1);

        return identifiers;
    }

}
