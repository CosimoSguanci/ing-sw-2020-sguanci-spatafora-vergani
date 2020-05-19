package it.polimi.ingsw.view.cli.components;

import it.polimi.ingsw.controller.commands.PlayerCommand;
import it.polimi.ingsw.view.cli.Cli;

public class RealGame {

    private final Cli cli;

    public RealGame(Cli cli) {
        this.cli = cli;
    }

    public void handleRealGame(String command) {
        PlayerCommand playerCommand = PlayerCommand.parseInput(command);
        cli.notify(playerCommand);
    }
}
