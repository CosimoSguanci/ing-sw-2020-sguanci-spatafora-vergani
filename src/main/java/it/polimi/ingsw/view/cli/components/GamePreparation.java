package it.polimi.ingsw.view.cli.components;

import it.polimi.ingsw.controller.commands.GamePreparationCommand;
import it.polimi.ingsw.view.cli.Cli;

public class GamePreparation {

    private final Cli cli;

    public GamePreparation(Cli cli) {
        this.cli = cli;
    }

    public void handle(String command) {

        GamePreparationCommand gamePreparationCommand = GamePreparationCommand.parseInput(command);
        cli.notify(gamePreparationCommand);

        cli.println("Wait for other players to place their Workers...");
    }
}
