package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.exceptions.BadCommandException;

/**
 * CommandType is the enumeration for the type of command a player can do.
 * In Santorini there are three type of possible commands: MOVE used to
 * move a player's worker to a different cell; BUILD used to build in a cell
 * according to relatives restrictions, END_TURN to declare the termination
 * of the turn and then the turn pass to another player.
 *
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 */
public enum CommandType {

    MOVE, BUILD, END_TURN, PICK, SELECT, PLACE, HELP, INFO, QUIT, TURN, RULES, GOD, BOARD;

    public static CommandType parseCommandType(String input) {
        try {
            return Enum.valueOf(CommandType.class, input.toUpperCase());
        } catch(IllegalArgumentException e) {
           if(input.toUpperCase().equals("END")) {
               return END_TURN;
           }
           else {
               throw new BadCommandException();
           }
        }
    }

    public static boolean isHelperCommandType(String command) {
        CommandType enumCommand;
        try {
            enumCommand = Enum.valueOf(CommandType.class, command.toUpperCase());
        } catch(IllegalArgumentException e) {
            return false;
        }
        return enumCommand.equals(CommandType.HELP) || enumCommand.equals(CommandType.INFO) || enumCommand.equals(CommandType.QUIT) || enumCommand.equals(CommandType.TURN) || enumCommand.equals(CommandType.RULES) || enumCommand.equals(CommandType.GOD) || enumCommand.equals(CommandType.BOARD);
    }
}
