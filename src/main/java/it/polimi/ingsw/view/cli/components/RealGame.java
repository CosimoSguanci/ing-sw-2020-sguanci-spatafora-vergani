package it.polimi.ingsw.view.cli.components;

import it.polimi.ingsw.controller.commands.PlayerCommand;
import it.polimi.ingsw.view.cli.Cli;

/**
 * This class manages REAL_GAME phase's commands.
 *
 * @author Andrea Vergani
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 */
public class RealGame {

    private final Cli cli;

    /**
     * This is the constructor of this class. At the moment of the creation
     * of a single instance of RealGame the cli associated to it is set
     * @param cli contains reference to the Cli associated
     */
    public RealGame(Cli cli) {
        this.cli = cli;
    }

    /**
     * This method handles commands during REAL_GAME game phase.
     * It makes a first parse of the input received from console and
     * if it is a valid one it notifies it to the controller
     * @param command contains a command received from console.
     */
    public void handleRealGame(String command) {
        PlayerCommand playerCommand = PlayerCommand.parseInput(command);
        cli.notify(playerCommand);
    }
}
