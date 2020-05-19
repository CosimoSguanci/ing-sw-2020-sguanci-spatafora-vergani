package it.polimi.ingsw.view.cli.components;

import it.polimi.ingsw.controller.GamePhase;
import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.exceptions.BadCommandException;
import it.polimi.ingsw.exceptions.UnknownGodException;
import it.polimi.ingsw.model.utils.GodsUtils;
import it.polimi.ingsw.view.Manual;
import it.polimi.ingsw.view.cli.Cli;

import java.util.Map;

public class CliPlayerHelper {
    private final Cli cli;

    public CliPlayerHelper(Cli cli){
        this.cli = cli;
    }

    public void helperHandle(String[] splitCommand) {
        if(CommandType.parseCommandType(splitCommand[0]) == CommandType.RULES && splitCommand.length == 1) {
            cli.newLine();
            cli.print(Cli.toBold("MANUAL"));
            cli.print(Manual.manual());
        }
        else if (CommandType.parseCommandType(splitCommand[0]) == CommandType.HELP && (splitCommand.length == 1)) { // todo extend help
            cli.print("If you want information about a specific game-phase: help <game-phase>");
            cli.print("Game-phases are: " + GamePhase.toStringBuilder());
            cli.print("Help Specific-Phase Example: help initial_info");
            cli.print("Help Current-Phase Example: help " + cli.getCurrentPhaseString());
            cli.print("(If a match hasn't started yet, help for current-phase may not be recognized, since there is no current phase!)");
            cli.print("help -> print command list and tutorial");
            cli.print("rules -> print game manual");
            cli.print("info <god> -> get info about a god");
            cli.print("Info Example: info Apollo");
        }
        else if (CommandType.parseCommandType(splitCommand[0]) == CommandType.HELP && (splitCommand.length == 2)  && (GamePhase.isGamePhase(splitCommand[1]) || infoAboutGamePhase(splitCommand[1]))) {
            if(infoAboutGamePhase(splitCommand[1])){
                helpString(cli.getCurrentPhase());
            }
            else{
                helpString(GamePhase.parseGamePhase(splitCommand[1]));
            }
        }

        else if (CommandType.parseCommandType(splitCommand[0]) == CommandType.TURN  && splitCommand.length == 1) {
            if(cli.getCurrentPhase() == GamePhase.REAL_GAME || cli.getCurrentPhase() == GamePhase.GAME_PREPARATION) {  //it's interesting to know who is playing only in these phases
                cli.printCurrentTurn();
            }
            else {
                cli.print("It is not the right moment for this command. Retry after match started");
            }
        }

        else if (CommandType.parseCommandType(splitCommand[0]) == CommandType.GOD  && splitCommand.length == 1) {
            if(cli.getCurrentPhase() == GamePhase.REAL_GAME || cli.getCurrentPhase() == GamePhase.GAME_PREPARATION) {  //it's interesting to know players-gods mapping only in these phases
                cli.printPlayerGods();
            }
            else {
                cli.print("It is not the right moment for this command. Retry after match started");
            }
        }
        else if (CommandType.parseCommandType(splitCommand[0]) == CommandType.BOARD  && splitCommand.length == 1) {
            if(cli.getCurrentPhase() == GamePhase.REAL_GAME || cli.getCurrentPhase() == GamePhase.GAME_PREPARATION) {  //it's interesting to see the board only in these phases
                cli.printBoard(cli.getCurrentBoard());
            }
            else {
                cli.print("It is not the right moment for this command. Retry after match started");
            }
        }
        else if (CommandType.parseCommandType(splitCommand[0]) == CommandType.INFO) {
            if (splitCommand.length != 2) {
                throw new BadCommandException();
            }
            String god = splitCommand[1];
            printGodInfo(god);
        }
        else if (CommandType.parseCommandType(splitCommand[0]) == CommandType.QUIT) {
            if (splitCommand.length > 1) {
                throw new BadCommandException();
            }
            cli.print("Quitting...");
            System.exit(0);
        }
    }

    private void helpString(GamePhase gamePhase) {
        switch (gamePhase) {
            case INITIAL_INFO:
                cli.print("In this phase, you decide your nickname and your workers' color");
                cli.print("Command format: pick <nickname> <color>");
                cli.print("Command example: pick Mike green");
                break;
            case CHOOSE_GODS:
                cli.print("In this phase, you select your god");
                cli.print("Command format: select <god>");
                cli.print("Command example: select Apollo");
                break;
            case GAME_PREPARATION:
                cli.print("In this phase, you place your workers on the board");
                cli.print("Command format: place w1 [letter][number] w2 [letter][number]");
                cli.print("Command example: place w1 A1 w2 B2");
                cli.print("Want to know who is playing in this moment? Type 'turn'");
                cli.print("Don't remember the association player <-> god? Type 'god'");
                cli.print("Got lost and want to see the current situation of the board? Type 'board'");
                break;
            case REAL_GAME:
                cli.print("In this phase, you play!");
                cli.print("Move format: move w1/w2 [letter][number] -> tries to move with the chosen Worker to the specified position");
                cli.print("Move example: move w1 A1");
                cli.print("Build format: build w1/w2 [letter][number] [optional: blockType {one, two, three, dome}] -> tries to build with the chosen Worker in the specified position");
                cli.print("Build example: build w1 A2");
                cli.print("Build example: build w1 A2 dome");
                cli.print("End format: end -> tries to end the current turn");
                cli.print("End example: end");
                cli.print("Want to know who is playing in this moment? Type 'turn'");
                cli.print("Don't remember the association player <-> god? Type 'god'");
                cli.print("Got lost and want to see the current situation of the board? Type 'board'");
                break;
            default:
                throw new BadCommandException();
        }
    }


    private boolean infoAboutGamePhase(String input) {
        return input.equals(cli.getCurrentPhaseString());
    }

    /**
     * This method is used to print information about a specific requested God.
     * @param god is the God whom information is requested.
     */
    private void printGodInfo(String god) {
        try {
            Map<String, String> godInfo = GodsUtils.parseGodName(god);
            cli.print("Name: " + godInfo.get(GodsUtils.GOD_NAME));
            cli.print("Description: " + godInfo.get(GodsUtils.GOD_DESCRIPTION));
            cli.print("Power: " + godInfo.get(GodsUtils.POWER_DESCRIPTION));
        } catch(UnknownGodException e) {
            cli.print("Unknown God Typed");
            throw new BadCommandException(); // todo ok ?
        }
    }
}
