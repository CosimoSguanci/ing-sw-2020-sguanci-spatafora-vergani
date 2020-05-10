package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.GamePhase;
import it.polimi.ingsw.controller.commands.InitialInfoCommand;
import it.polimi.ingsw.model.PrintableColour;
import it.polimi.ingsw.model.updates.*;
import it.polimi.ingsw.model.utils.GodsUtils;
import it.polimi.ingsw.view.UpdateHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CliUpdateHandler implements UpdateHandler {
    private final Cli cliInstance;

    CliUpdateHandler(Cli cliInstance) {
        this.cliInstance = cliInstance;
    }

    public void handle(MatchStartedUpdate update) {
        cliInstance.printBoard(update.board);

        cliInstance.print(Cli.toBold("MATCH STARTED!"));
        cliInstance.newLine();
        cliInstance.print(Cli.toBold("Tip") + ": type 'help " + cliInstance.getCurrentPhaseString() + "' to know command format");
        cliInstance.newLine();
        cliInstance.printCurrentTurn();
    }

    public void handle(ChooseGodsUpdate update) {

        if (update.isGodChooser) {
            cliInstance.setInitialGodChooser(true);
            cliInstance.print(Cli.toBold("Choose ") + cliInstance.getPlayersNum() + " gods. For a perfect match, choose the ones you like most!    Command " + Cli.toBold("format") + " expected: select [god(1)] ... [god(n)]");
            cliInstance.print("Available gods are: " + availableGods());
            cliInstance.print("REMEMBER: if you need info about a god and his/her powers, type 'info [god]'");
            cliInstance.newLine();
        } else {
            cliInstance.setInitialGodChooser(false);
            cliInstance.setSelectableGods(update.selectableGods);
            cliInstance.print(Cli.toBold("Choose ") + "your god.    Command " + Cli.toBold("format") + " expected: select [god]");
            if(listToStringBuilder(update.selectableGods) != null) {
                cliInstance.print("Available choices are: " + listToStringBuilder(update.selectableGods));
            }
            //update.selectableGods.forEach(System.out::println);
            cliInstance.print("REMEMBER: if you need info about a god, type 'info [god]'");
            cliInstance.newLine();
        }
    }

    public void handle(SelectedGodsUpdate update) {
        cliInstance.setPlayersGods(update.selectedGods);
        cliInstance.printPlayerGods();
    }

    public void handle(SelectedInitialInfoUpdate update) {
        cliInstance.setPlayersColors(update.initialInfo);
        cliInstance.printPlayersColors();
    }

    public void handle(GamePreparationUpdate update) {
        cliInstance.print("Game Preparation: place your " + Cli.toBold("workers") + ".    Command " + Cli.toBold("format") + " expected: place W1 [row1][col1]  W2 [row2][col2]");
        cliInstance.newLine();
    }

    public void handle(BoardUpdate update) {
        cliInstance.printBoard(update.board);
        cliInstance.printCurrentTurn();
    }

    public void handle(ErrorUpdate update) {
        switch (update.command) {
            case MOVE:
                cliInstance.print(Cli.toBold("Move Error") + ": maybe you can't perform this move or it's not time for a move. Try with another command or wait for your turn!");
                break;

            case BUILD:
                cliInstance.print(Cli.toBold("Build Error") + ": maybe you can't perform this build or it's not time for a build. Try with another command or wait for your turn!");
                break;

            case END_TURN:
                cliInstance.print("You " + Cli.toBold("can't end") +" your turn now");
                break;

            case PICK:
                cliInstance.print(Cli.toBold("Nickname/Color Choose Error") + ": maybe it's not your turn and/or you typed something incorrectly");
                break;

            case PLACE:
                cliInstance.print(Cli.toBold("Game Preparation Error") + ": maybe it's not your turn and/or you typed something incorrectly");
                break;

            case SELECT:
                cliInstance.print(Cli.toBold("Gods Choose Error") + ": maybe it's not your turn and/or you typed something incorrectly");
                break;
        }
        cliInstance.print("For more information about commands or rules, type 'help " + cliInstance.getCurrentPhaseString() + "' or 'rules'");
        cliInstance.newLine();
    }

    public void handle(GamePhaseChangedUpdate update) {
        cliInstance.newLine();
        cliInstance.newLine();
        cliInstance.newLine();
        if(update.newGamePhase.isPrintable()) {
            cliInstance.print(Cli.toBold(update.newGamePhase.toString()));
        }
        cliInstance.setCurrentGamePhase(update.newGamePhase);
    }

    public void handle(InitialInfoUpdate update) {
       cliInstance.print("Type your " + Cli.toBold("nickname and color") + " separated by a space.    Command " + Cli.toBold("format") + " expected: pick [nickname] [color]");

       if(!update.selectedNicknames.isEmpty()) {
           cliInstance.print("Nicknames already taken are: " + listToStringBuilder(update.selectedNicknames));
           //update.selectedNicknames.forEach(System.out::println);
       }

       cliInstance.setSelectedNicknames(update.selectedNicknames);
       cliInstance.print("Available colors are: ");

       update.selectableColors.forEach((color) -> {
            cliInstance.print(Cli.convertColorToAnsi(color) + color + PrintableColour.RESET);
        });

       //update.selectableColors.forEach(System.out::println);

       cliInstance.setSelectableColors(update.selectableColors);
       cliInstance.newLine();
    }

    public void handle(PlayerUpdate update) {
        cliInstance.forwardNotify(update);
    }

    public void handle(TurnUpdate update) {
        /*if(cliInstance.controller.getClientPlayerID().equals(update.currentPlayerID)) {
            cliInstance.print("Your Turn");
        } else {
            cliInstance.print("Turn ended. Wait for your turn...");
        }*/
        cliInstance.forwardNotify(update);
        //cliInstance.printCurrentTurn();
    }

    public void handle(WinUpdate update) {
        cliInstance.newLine();
        cliInstance.print(cliInstance.playerWithColor(update.winnerPlayerNickname) + " wins!");

        cliInstance.newLine();
        cliInstance.print("Do you want to play another match?");

        //cliInstance.handleMatchEnded();
    }

    public void handle(LoseUpdate update) {

        if(cliInstance.controller.getClientPlayerID().equals(update.loserPlayerID)) {
            cliInstance.newLine();
            cliInstance.print(Cli.toBold("You lost!"));
            cliInstance.newLine();
            cliInstance.setCurrentGamePhase(GamePhase.MATCH_LOST);

            if(update.onePlayerRemaining) {
                cliInstance.setCurrentGamePhase(GamePhase.MATCH_ENDED); // todo method to avoid duplicate
                cliInstance.print("Do you want to play another match?");
            }
            else {
                cliInstance.print("Do you want to continue to watch this match?");
            }
        }
        else if(update.onePlayerRemaining) {
            cliInstance.newLine();
            cliInstance.print(cliInstance.playerWithColor(update.loserPlayerNickname) + " lost!");
            cliInstance.print(Cli.toBold("You Win!"));
            cliInstance.newLine();
            cliInstance.setCurrentGamePhase(GamePhase.MATCH_ENDED);
            cliInstance.print("Do you want to play another match?");
        }


    }

    public void handle(ServerUnreachableUpdate update) {
        cliInstance.print("Cannot communicate to the Server, maybe it's down. Otherwise, check your connection.");
        cliInstance.print("Quitting..."); // todo method quit()
        System.exit(0);
    }

    public void handle(DisconnectedPlayerUpdate update) {
        String nicknameToShow = update.disconnectedPlayerNickname != null ? update.disconnectedPlayerNickname : "A player";
        cliInstance.print(nicknameToShow + " disconnected!");

       if(update.onePlayerRemaining) {
            cliInstance.newLine();
            cliInstance.print(Cli.toBold("You Win!"));
            cliInstance.newLine();
            cliInstance.setCurrentGamePhase(GamePhase.MATCH_ENDED);
            cliInstance.print("Do you want to play another match?");
        }

    }


    private StringBuilder listToStringBuilder(List<String> value) {
        if(value.size() == 0) {
            return null;
        }
        StringBuilder result = new StringBuilder(value.get(0));
        for(int i = 1; i < value.size(); i++) {
            result.append(", ").append(value.get(i));
        }
        return result;
    }

    private StringBuilder availableGods() {
        HashMap<String, HashMap<String, String>> godsInfo = new HashMap(GodsUtils.getGodsInfo());  //all info about available gods
        ArrayList<String> godNames = new ArrayList(godsInfo.keySet());  //list of gods' names
        StringBuilder result = new StringBuilder(godNames.get(0).toUpperCase());
        for(int i = 1; i < godNames.size(); i++) {
            result.append(", ").append(godNames.get(i).toUpperCase());
        }
        return result;
    }

}

