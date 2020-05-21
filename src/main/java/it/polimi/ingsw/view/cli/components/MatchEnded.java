package it.polimi.ingsw.view.cli.components;

import it.polimi.ingsw.exceptions.BadCommandException;
import it.polimi.ingsw.view.cli.Cli;

import java.io.IOException;

public class MatchEnded {
    private final Cli cli;

    public MatchEnded(Cli cli){
        this.cli = cli;
    }

    private void manage(){
        try {
            cli.getClient().getUpdateListener().setIsActive(false);
            cli.getClient().reinitializeConnection();

         //   cli.getClient().getUpdateListener().addObserver(cli);

            cli.setPlayersNum(0);
            cli.setContinueToWatch(false);

        } catch(IOException e) {
            e.printStackTrace();
            System.err.println("The Game couldn't start, maybe there was some network error or the server isn't available.");
            System.exit(0);
        }
        cli.start();
    }

    public boolean handle(String command) {
        if(command.equals("yes")) {
            manage();
            return true;        //break while in cli
        }
        else if(command.equals("no")) {
            cli.print("Quitting...");
            System.exit(0);
        }
        else throw new BadCommandException();
        return false;
    }

}
