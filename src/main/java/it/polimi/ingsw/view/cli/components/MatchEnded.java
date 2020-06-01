package it.polimi.ingsw.view.cli.components;

import it.polimi.ingsw.exceptions.BadCommandException;
import it.polimi.ingsw.view.cli.Cli;

import java.io.IOException;

/**
 * This class deals with MATCH_ENDED game phase.
 * It manages all the choices made by players. It allows players
 * to start a new match once the match in which they were involved is finished.
 */
public class MatchEnded {
    private final Cli cli;

    /**
     * This is the constructor of this class. At the moment of the creation
     * of a single instance of MatchEnded the cli associated to it is set
     * @param cli contains reference to the Cli associated
     */
    public MatchEnded(Cli cli){
        this.cli = cli;
    }

    /**
     * This method manages answers from users once the match ends.
     * If a user type "yes" in this part it means that he wants to play another
     * match. Otherwise, answering "no" to the question proposed, the user
     * express its will to quit.
     * @param command contains the answer from the user
     * @return true if the command received was "yes",
     *         false otherwise.
     */
    public boolean handle(String command) {
        if(command.equals("yes")) {
            manage();
            return true;        //break while in cli
        }
        else if(command.equals("no")) {
            cli.println("Quitting...");
            System.exit(0);
        }
        else throw new BadCommandException();
        return false;
    }

    /**
     * This private method manages the case in which a user answers
     * "yes" to the question "Do you want to play another match?" once
     * his match ended.
     */
    private void manage(){
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
