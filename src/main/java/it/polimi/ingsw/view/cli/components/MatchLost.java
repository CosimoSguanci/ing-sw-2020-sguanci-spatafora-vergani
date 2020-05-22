package it.polimi.ingsw.view.cli.components;

import it.polimi.ingsw.controller.GamePhase;
import it.polimi.ingsw.exceptions.BadCommandException;
import it.polimi.ingsw.view.cli.Cli;

public class MatchLost {
    private final Cli cli;

    public MatchLost(Cli cli) {
        this.cli = cli;
    }

    public boolean handleMatchLost(String command) {
        if(command.equals("yes")) {
            cli.setContinueToWatch(true);
            cli.newLine();
            return true;
        }
        else if(command.equals("no")) {
            cli.setCurrentGamePhase(GamePhase.MATCH_ENDED);
            cli.newLine();
            cli.println("Do you want to play another match?");
        }
        else throw new BadCommandException(); // todo add multiple exception
        return false;
    }
}
