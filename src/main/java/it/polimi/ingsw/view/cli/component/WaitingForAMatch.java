package it.polimi.ingsw.view.cli.component;

import it.polimi.ingsw.view.cli.Cli;

public class WaitingForAMatch {

    private final Cli cli;

    public WaitingForAMatch(Cli cli) {
        this.cli = cli;
    }

    public void handleWaiting() {
        cli.print("Waiting for a match...");
        cli.newLine();
        cli.print("HIGHLY RECOMMENDED: type '" + Cli.toBold("help") + "' in any moment to have information about game-phases, commands, ...");
        cli.print("RECOMMENDED: if it's the first time you play or have any doubt during the game, type '" + Cli.toBold("rules") + "' to read a short version of Santorini's manual");
        cli.newLine();
    }
}
