package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.GamePhase;
import it.polimi.ingsw.controller.commands.InitialInfoCommand;
import it.polimi.ingsw.model.PrintableColour;
import it.polimi.ingsw.model.updates.*;
import it.polimi.ingsw.view.UpdateHandler;

public class CliUpdateHandler implements UpdateHandler {
    private final Cli cliInstance;

    CliUpdateHandler(Cli cliInstance) {
        this.cliInstance = cliInstance;
    }

    public void handle(MatchStartedUpdate update) {
        cliInstance.printBoard(update.board);

        cliInstance.print("Match Started");
    }

    public void handle(ChooseGodsUpdate update) {

        if (update.isGodChooser) {
            cliInstance.setInitialGodChooser(true);
            cliInstance.print("Choose " + cliInstance.getPlayersNum() + " gods...");
        } else {
            cliInstance.setInitialGodChooser(false);
            cliInstance.setSelectableGods(update.selectableGods);
            cliInstance.print("Choose your god. Available choices are: ");
            update.selectableGods.forEach(System.out::println);
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
        cliInstance.print("Game Preparation: place your workers ");
    }

    public void handle(BoardUpdate update) {
        cliInstance.printBoard(update.board);
        cliInstance.printCurrentTurn();
    }

    public void handle(ErrorUpdate update) {
        switch (update.command) {
            case MOVE:
                cliInstance.print("Move Error");
                break;

            case BUILD:
                cliInstance.print("Build Error");
                break;

            case END_TURN:
                cliInstance.print("You can't end your turn");
                break;

            case PICK:
                cliInstance.print("Nickname/Color choose Error: check if it's your turn");
                break;

            case PLACE:
                cliInstance.print("Game Preparation Error: check if it's your turn");
                break;

            case SELECT:
                cliInstance.print("Gods Choose Error: check if it's your turn");
                break;
        }
    }

    public void handle(GamePhaseChangedUpdate update) {
        cliInstance.setCurrentGamePhase(update.newGamePhase);
    }

    public void handle(InitialInfoUpdate update) {
       cliInstance.print("Type your Nickname and color separated by a space.    Command format expected: pick [nickname] [color]");

       if(!update.selectedNicknames.isEmpty()) {
           cliInstance.print("Nicknames already taken are: ");
           update.selectedNicknames.forEach(System.out::println);
       }

       cliInstance.setSelectedNicknames(update.selectedNicknames);
       cliInstance.print("Available colors are: ");

       update.selectableColors.forEach((color) -> {
            cliInstance.print(Cli.convertColorToAnsi(color) + color + PrintableColour.RESET);
        });

       //update.selectableColors.forEach(System.out::println);

       cliInstance.setSelectableColors(update.selectableColors);
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
        cliInstance.print(update.winnerPlayerNickname + " wins!");

        cliInstance.print("Do you want to play another match?");

        //cliInstance.handleMatchEnded();
    }

    public void handle(LoseUpdate update) {

        if(cliInstance.controller.getClientPlayerID().equals(update.loserPlayerID)) {
            cliInstance.print("You lost!");
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
            cliInstance.print(update.loserPlayerNickname + " lost!");
            cliInstance.print("You Win!");
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
            cliInstance.print("You Win!");
            cliInstance.setCurrentGamePhase(GamePhase.MATCH_ENDED);
            cliInstance.print("Do you want to play another match?");
        }

    }

}

