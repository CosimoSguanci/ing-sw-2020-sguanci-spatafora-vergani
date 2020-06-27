package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.exceptions.BadCommandException;

/**
 * CommandType is the enumeration for the type of command a player can do.
 * In Santorini there are different type of possible commands: MOVE, used to
 * move a player's worker to a different cell; BUILD, used to build in a cell
 * according to its restrictions; END_TURN, to declare the end of the turn (and
 * then the turn passes to another player); PICK, to select nickname and colour;
 * SELECT, to choose a god/some gods; PLACE, to decide the initial position of the
 * workers on the board; HELP, to receive some information about what to do; INFO,
 * to have more specific information; QUIT, to leave a match; TURN, to "ask" who is
 * playing his/her turn; RULES, to read a short version of Santorini's manual; GOD,
 * to know the association player <-> god during a match; BOARD, to see the current
 * board (and situation) of the match.
 *
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 */
public enum CommandType {

    MOVE, BUILD, END_TURN, PICK, SELECT, PLACE, HELP, INFO, QUIT, TURN, RULES, GOD, BOARD;


    /**
     * This static method parses a string and sees if it corresponds to one of the CommandTypes.
     * Matching takes place when the string is equal to a sort of .toString for CommandType; note
     * that the string "end" is considered to match with CommandType.END_TURN.
     *
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
