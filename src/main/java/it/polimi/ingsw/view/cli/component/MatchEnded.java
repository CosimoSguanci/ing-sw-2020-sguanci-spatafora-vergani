package it.polimi.ingsw.view.cli.component;

import it.polimi.ingsw.exceptions.BadCommandException;
import it.polimi.ingsw.view.cli.Cli;

import java.io.IOException;

public class MatchEnded {
    private Cli cli;

    public MatchEnded(Cli cli){
        this.cli = cli;
    }

    public void handle(){
        try {
            cli.getClient().getUpdateListener().setIsActive(false);
            cli.getClient().reinitializeConnection();

            cli.setPlayersNum(0);
            cli.setContinueToWatch(false);

        } catch(IOException e) {
            e.printStackTrace();
            System.err.println("The Game couldn't start, maybe there was some network error or the server isn't available.");
            System.exit(0);
        }
        cli.start();
    }
}
