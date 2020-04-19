package it.polimi.ingsw.controller;

/**
 * CommandType is the enumeration for the type of command a player can do.
 * In Santorini there are three type of possible commands: MOVE used to
 * move a player's worker to a different cell; BUILD used to build in a cell
 * according to relatives restrictions, END_TURN to declare the termination
 * of the turn and then the turn pass to another player.
 *
 * @author Roberto Spatafora
 */
public enum CommandType {

    MOVE, BUILD, END_TURN;

    public static CommandType parseCommandType(String input) {
        return Enum.valueOf(CommandType.class, input.toUpperCase());
    }
}
