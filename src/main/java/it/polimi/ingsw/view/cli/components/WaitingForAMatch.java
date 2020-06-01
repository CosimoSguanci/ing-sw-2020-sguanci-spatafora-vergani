package it.polimi.ingsw.view.cli.components;

import it.polimi.ingsw.view.cli.Cli;

/**
 * This class manages all the messages to print when a player
 * waits for others to arrive.
 *
 * @author Andrea Vergani
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 */
public class WaitingForAMatch {

    private final Cli cli;

    /**
     * This is the constructor method of the class.
     * It make an association between an instance of this class
     * and the Cli associated to it.
     * @param cli is the instance of Cli associated to GamePreparation.
     */
    public WaitingForAMatch(Cli cli) {
        this.cli = cli;
    }

    /**
     * This method prints information to a player who is
     * waiting for others to arrive.
     */
    public void handleWaiting() {
        cli.println("Waiting for a match...");
        cli.newLine();
        cli.println("HIGHLY RECOMMENDED: type '" + Cli.toBold("help") + "' in any moment to have information about game-phases, commands, ...");
        cli.println("RECOMMENDED: if it's the first time you play or have any doubt during the game, type '" + Cli.toBold("rules") + "' to read a short version of Santorini's manual");
        cli.newLine();
    }
}
