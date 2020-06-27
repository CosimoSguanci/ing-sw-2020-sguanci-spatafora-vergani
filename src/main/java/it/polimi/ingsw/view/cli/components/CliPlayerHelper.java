package it.polimi.ingsw.view.cli.components;

import it.polimi.ingsw.controller.GamePhase;
import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.exceptions.BadCommandException;
import it.polimi.ingsw.exceptions.UnknownGodException;
import it.polimi.ingsw.model.utils.GodsUtils;
import it.polimi.ingsw.view.Manual;
import it.polimi.ingsw.view.cli.Cli;

import java.util.Map;

/**
 * This class is made to make Cli user-friendly, answering
 * to every possible help request from users.
 * This class specify a game manual, if someone is new to the game.
 * CommandType.RULES, CommandType.HELP, CommandType.TURN, CommandType.GOD,
 * CommandType.BOARD, CommandType.INFO are managed in this class by
 * printing what a user requires.
 *
 * @author Andrea Vergani
 * @author Roberto Spatafora
 */
public class CliPlayerHelper {
    private final Cli cli;

    /**
     * This is the constructor of this class. At the moment of the creation
     * of a single instance of CliPlayerHelper the cli associated to it is set
     *
     * @param cli contains reference to the Cli associated
     */
    public CliPlayerHelper(Cli cli) {
        this.cli = cli;
    }

    /**
     * This method prints what is requested from users, responding to command as
     * RULES, HELP, TURN, GOD, BOARD, INFO.
     *
     * @param splitCommand is the split string, from the entire one, received as command from user
     */
    public void helperHandle(String[] splitCommand) {
        if (CommandType.parseCommandType(splitCommand[0]) == CommandType.RULES && splitCommand.length == 1) {
            cli.newLine();
            cli.println(Cli.toBold("MANUAL"));
            cli.println(Manual.manual());
        } else if (CommandType.parseCommandType(splitCommand[0]) == CommandType.HELP && (splitCommand.length == 1)) { // todo extend help
            cli.println("If you want information about a specific game-phase: help <game-phase>");
            cli.println("Game-phases are: " + GamePhase.toStringBuilder());
            cli.println("Help Specific-Phase Example: help initial_info");
            cli.println("Help Current-Phase Example: help " + cli.getCurrentPhaseString());
            cli.println("(If a match hasn't started yet, help for current-phase may not be recognized, since there is no current phase!)");
            cli.println("help -> print command list and tutorial");
            cli.println("rules -> print game manual");
            cli.println("info <god> -> get info about a god");
            cli.println("Info Example: info Apollo");
        } else if (CommandType.parseCommandType(splitCommand[0]) == CommandType.HELP && (splitCommand.length == 2) && (GamePhase.isGamePhase(splitCommand[1]) || infoAboutGamePhase(splitCommand[1]))) {
            if (infoAboutGamePhase(splitCommand[1])) {
                helpString(cli.getCurrentPhase());
            } else {
                helpString(GamePhase.parseGamePhase(splitCommand[1]));
            }
        } else if (CommandType.parseCommandType(splitCommand[0]) == CommandType.TURN && splitCommand.length == 1) {
            if (cli.getCurrentPhase() == GamePhase.REAL_GAME || cli.getCurrentPhase() == GamePhase.GAME_PREPARATION) {  //it's interesting to know who is playing only in these phases
                cli.printCurrentTurn();
            } else {
                cli.println("It is not the right moment for this command. Retry after match started");
            }
        } else if (CommandType.parseCommandType(splitCommand[0]) == CommandType.GOD && splitCommand.length == 1) {
            if (cli.getCurrentPhase() == GamePhase.REAL_GAME || cli.getCurrentPhase() == GamePhase.GAME_PREPARATION) {  //it's interesting to know players-gods mapping only in these phases
                cli.printPlayerGods();
            } else {
                cli.println("It is not the right moment for this command. Retry after match started");
            }
        } else if (CommandType.parseCommandType(splitCommand[0]) == CommandType.BOARD && splitCommand.length == 1) {
            if (cli.getCurrentPhase() == GamePhase.REAL_GAME || cli.getCurrentPhase() == GamePhase.GAME_PREPARATION) {  //it's interesting to see the board only in these phases
                cli.printBoard(cli.getCurrentBoard());
            } else {
                cli.println("It is not the right moment for this command. Retry after match started");
            }
        } else if (CommandType.parseCommandType(splitCommand[0]) == CommandType.INFO) {
            if (splitCommand.length != 2) {
                throw new BadCommandException();
            }
            String god = splitCommand[1];
            printGodInfo(god);
        } else if (CommandType.parseCommandType(splitCommand[0]) == CommandType.QUIT) {
            if (splitCommand.length > 1) {
                throw new BadCommandException();
            }
            cli.println("Quitting...");
            System.exit(0);
        }
    }

    /**
     * This private method, prints different strings based on gamePhase
     *
     * @param gamePhase indicates the gamePhase that distinguishes which
     *                  string is to be printed.
     */
    private void helpString(GamePhase gamePhase) {
        switch (gamePhase) {
            case INITIAL_INFO:
                cli.println("In this phase, you decide your nickname and your workers' color");
                cli.println("Command format: pick <nickname> <color>");
                cli.println("Command example: pick Mike green");
                break;
            case CHOOSE_GODS:
                cli.println("In this phase, you select your god");
                cli.println("Command format: select <god>");
                cli.println("Command example: select Apollo");
                break;
            case GAME_PREPARATION:
                cli.println("In this phase, you place your workers on the board");
                cli.println("Command format: place w1 [letter][number] w2 [letter][number]");
                cli.println("Command example: place w1 A1 w2 B2");
                cli.println("Want to know who is playing in this moment? Type 'turn'");
                cli.println("Don't remember the association player <-> god? Type 'god'");
                cli.println("Got lost and want to see the current situation of the board? Type 'board'");
                break;
            case REAL_GAME:
                cli.println("In this phase, you play!");
                cli.println("Move format: move w1/w2 [letter][number] -> tries to move with the chosen Worker to the specified position");
                cli.println("Move example: move w1 A1");
                cli.println("Build format: build w1/w2 [letter][number] [optional: blockType {one, two, three, dome}] -> tries to build with the chosen Worker in the specified position");
                cli.println("Build example: build w1 A2");
                cli.println("Build example: build w1 A2 dome");
                cli.println("End format: end -> tries to end the current turn");
                cli.println("End example: end");
                cli.println("Want to know who is playing in this moment? Type 'turn'");
                cli.println("Don't remember the association player <-> god? Type 'god'");
                cli.println("Got lost and want to see the current situation of the board? Type 'board'");
                break;
            default:
                throw new BadCommandException();
        }
    }

    /**
     * @param input should indicates a GamePhase. More general it can be a String
     * @return true if the string received as parameter indicates the currentGamePhase
     * false otherwise.
     */
    private boolean infoAboutGamePhase(String input) {
        return input.equals(cli.getCurrentPhaseString());
    }

    /**
     * This method is used to print information about a specific requested God.
     *
     * @param god is the God whom information is requested.
     */
    private void printGodInfo(String god) {
        try {
            Map<String, String> godInfo = GodsUtils.parseGodName(god);
            cli.println("Name: " + godInfo.get(GodsUtils.GOD_NAME));
            cli.println("Description: " + godInfo.get(GodsUtils.GOD_DESCRIPTION));
            cli.println("Power: " + godInfo.get(GodsUtils.POWER_DESCRIPTION));
        } catch (UnknownGodException e) {
            cli.println("Unknown God Typed");
            throw new BadCommandException(); // todo ok ?
        }
    }
}
