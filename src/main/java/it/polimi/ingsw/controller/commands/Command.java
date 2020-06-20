package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.CommandHandler;
import it.polimi.ingsw.exceptions.BadCommandException;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * Command is an abstract class extended by all sort of commands addressed to Controller (Server) by Clients.
 * Every Command is characterized by a type, the player who sends it (nickname and ID) and the proper message.
 * A Command is used by Clients to communicate with Server. For this reason, this class is Serializable.
 *
 * @author Cosimo Sguanci
 */
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

    /**
     * The constructor simply creates a command of the specified type.
     *
     * @param commandType the type of command that must be created
     */
    protected Command(CommandType commandType) {
        this.commandType = commandType;
    }

    /**
     * This method sets the player, who is the one who sends the command.
     *
     * @param player the sender
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * This method is the getter for the player who sent the command.
     *
     * @return the sender-player
     */
    public Player getPlayer() {
        return this.player;
    }


    /**
     * This method sets the playerID of the player who sends the command.
     *
     * @param playerID sender's playerID
     */
    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }


    /**
     * This method is the getter for the playerID of the player who sent the command.
     *
     * @return the sender's playerID
     */
    public String getPlayerID() {
        return this.playerID;
    }


    /**
     * This method sets the player-nickname of the player who sends the command.
     *
     * @param playerNickname sender's nickname
     */
    public void setPlayerNickname(String playerNickname) {
        this.playerNickname = playerNickname;
    }


    /**
     * This method is the getter for the nickname of the player who sent the command.
     *
     * @return the sender's nickname
     */
    public String getPlayerNickname() {
        return this.playerNickname;
    }


    /**
     * This method handles the command received by Server in the proper way.
     *
     * @param handler the object that is going to handle the command
     */
    public abstract void handleCommand(CommandHandler handler);

    /**
     * This static method handles the conversion between board's cell identification in the form of
     * "BattleShip" to identification in the form of matrix. For example, "BattleShip" cell "A1"
     * corresponds to (0,0) positions in a matrix, "B3" maps (1,2) and so on...
     *
     * @param partialCommand a string that represents a cell in the form "letter" + "number"
     *                       ("BattleShip" form)
     * @return a map for parameter-cell's position in a matrix
     * @throws BadCommandException if parameter-string does not represent a valid command
     */
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
