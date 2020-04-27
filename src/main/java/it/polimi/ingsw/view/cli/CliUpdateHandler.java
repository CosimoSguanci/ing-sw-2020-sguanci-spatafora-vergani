package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.updates.*;
import it.polimi.ingsw.view.UpdateHandler;

public class CliUpdateHandler implements UpdateHandler {
    private final Cli cliInstance;

    CliUpdateHandler(Cli cliInstance) {
        this.cliInstance = cliInstance;
    }

    public void handle(MatchStartedUpdate update) {
        cliInstance.printBoard(update.board);
        cliInstance.setEnableGamePreparation(false);
        cliInstance.setEnableGameCommands(true);

        cliInstance.print("Match Started");
    }

    public void handle(ChooseGodsUpdate update) {
        cliInstance.setEnableGodChoose(true);

        if (update.isGodChooser) {
            cliInstance.setInitialGodChooser(true);
            cliInstance.print("Choose " + cliInstance.getPlayersNum() + " gods...");
        } else {
            cliInstance.setInitialGodChooser(false);
            cliInstance.setSelectableGods(update.selectableGods);
            cliInstance.print("Choose your god. Available choices are: ");
            cliInstance.getSelectableGods().forEach(System.out::println);
        }
    }

    public void handle(SelectedGodsUpdate update) {
        cliInstance.setPlayersGods(update.selectedGods);
        cliInstance.printPlayerGods();
    }

    public void handle(GamePreparationUpdate update) {
        cliInstance.setEnableGodChoose(false);
        cliInstance.setEnableGamePreparation(false);
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
        }
    }

    public void handle(PlayerUpdate update) {
        cliInstance.forwardNotify(update);
    }

    public void handle(TurnUpdate update) {
        cliInstance.forwardNotify(update);
    }
}

