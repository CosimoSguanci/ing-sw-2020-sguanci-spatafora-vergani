package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.exceptions.BadCommandException;

/**
 * CommandType is the enumeration for the type of command a player can do.
 * In Santorini there are different type of possible commands.
 *
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 */
public enum CommandType {

    /**
     * Used to move a player's worker to a different cell.
     */
    MOVE,

    /**
     * Used to build in a cell according to its restrictions.
     */
    BUILD,

    /**
     * Used to declare the end of the turn (and then the turn passes to another player).
     */
    END_TURN,

    /**
     * Used to select nickname and color.
     */
    PICK,

    /**
     * Used to choose a god or some gods if it's the god chooser.
     */
    SELECT,

    /**
     * Used to decide the initial position of the workers on the board.
     */
    PLACE,

    /**
     * Used to receive some information about what to do.
     */
    HELP,

    /**
     * Used to have more specific information than HELP.
     */
    INFO,

    /**
     * Used to leave a match.
     */
    QUIT,

    /**
     * Used to "ask" who is playing his/her turn.
     */
    TURN,

    /**
     * Used to read a short version of Santorini's manual.
     */
    RULES,

    /**
     * Used to know the association between player and god during a match.
     */
    GOD,

    /**
     * Used to see the current board (and situation) of the match.
     */
    BOARD;


    /**
     * This static method parses a string and sees if it corresponds to one of the CommandTypes.
     * Matching takes place when the string is equal to a sort of .toString for CommandType; note
     * that the string "end" is considered to match with CommandType.END_TURN.
     *
     * @param input the String that the user typed
     * @return the matching command-type
     * @throws BadCommandException if parameter string does not represent any CommandType
     */
    public static CommandType parseCommandType(String input) {
        try {
            return Enum.valueOf(CommandType.class, input.toUpperCase());
        } catch (IllegalArgumentException e) {
            if (input.toUpperCase().equals("END")) {
                return END_TURN;
            } else {
                throw new BadCommandException();
            }
        }
    }

    /**
     * This static method parses a string and sees if it corresponds to one of the CommandTypes of the
     * "Help class"; this "class" is composed by all sort of CommandTypes that give information in order
     * to help to play a better match.
     * Matching takes place when the string is equal to a sort of .toString for CommandType.
     *
     * @param command the String that the user typed
     * @return true if the string matches a CommandType of "Help class", false otherwise
     */
    public static boolean isHelperCommandType(String command) {
        CommandType enumCommand;
        try {
            enumCommand = Enum.valueOf(CommandType.class, command.toUpperCase());
        } catch (IllegalArgumentException e) {
            return false;
        }
        return enumCommand.equals(CommandType.HELP) || enumCommand.equals(CommandType.INFO) || enumCommand.equals(CommandType.QUIT) || enumCommand.equals(CommandType.TURN) || enumCommand.equals(CommandType.RULES) || enumCommand.equals(CommandType.GOD) || enumCommand.equals(CommandType.BOARD);
    }
}
