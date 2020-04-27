package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.updates.*;
import it.polimi.ingsw.view.UpdateHandler;

public class CliUpdateHandler implements UpdateHandler {
    private final Cli cliInstance;

    CliUpdateHandler(Cli cliInstance) {
        this.cliInstance = cliInstance;
    }

    public void handle(MatchStartedUpdate update) {

    }
    public void handle(ChooseGodsUpdate update) {

    }
    public void handle(SelectedGodsUpdate update) {

    }
    public void handle(GamePreparationUpdate update) {

    }
    public void handle(BoardUpdate update) {

    }
    public void handle(ErrorUpdate update) {

    }
    public void handle(PlayerUpdate update) {
        cliInstance.forwardNotify(update);
    }
    public void handle(TurnUpdate update) {
        cliInstance.forwardNotify(update);
    }
}

