package it.polimi.ingsw.view.cli.components;

import it.polimi.ingsw.controller.GamePhase;
import it.polimi.ingsw.exceptions.BadCommandException;
import it.polimi.ingsw.view.cli.Cli;

/**
 * This class deals with MATCH_LOST game phase.
 * It manages all the choices made by players. It allows players to continue to
 * watch the match in which they were involved, even if they have already lost.
 */
public class MatchLost {
    private final Cli cli;

    /**
     * This is the constructor of this class. At the moment of the creation
     * of a single instance of MatchLost the cli associated to it is set
     * @param cli contains reference to the Cli associated
     */
    public MatchLost(Cli cli) {
        this.cli = cli;
    }

    /**
     * This method manages answers from users once they lose.
     * If a user type "yes" in this phase it means that he wants to continue to
     * watch the match. Otherwise, answering "no" to the question proposed,
     * the user express its will to quit.
     * @param command contains the answer from the user
     * @return true if the command received was "yes",
     *         false otherwise.
     */
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
