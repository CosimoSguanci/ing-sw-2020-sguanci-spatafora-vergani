package it.polimi.ingsw.view.cli;

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
       cliInstance.print("Type your Nickname and color separated by a space");

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
        cliInstance.forwardNotify(update);
    }


}

