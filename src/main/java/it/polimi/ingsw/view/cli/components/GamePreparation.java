package it.polimi.ingsw.view.cli.components;

import it.polimi.ingsw.controller.commands.GamePreparationCommand;
import it.polimi.ingsw.view.cli.Cli;

/**
 * This class manages GamePreparation phase's commands.
 * This class has a reference to the Cli associated, once received a
 * command during GAME_PREPARATION game phase go through this class
 *
 *  * @author Andrea Vergani
 *  * @author Roberto Spatafora
 */
public class GamePreparation {

    private final Cli cli;

    /**
     * This is the constructor method of the class.
     * It make an association between an instance of this class
     * and the Cli associated to it.
     * @param cli is the instance of Cli associated to GamePreparation.
     */
    public GamePreparation(Cli cli) {
        this.cli = cli;
    }

    /**
     * This method parses the input and once it receive a well formed command
     * it notifies to the Cli, printing to wait for other players to do the same,
     * placing their workers.
     * @param command
     */
    public void handle(String command) {

        GamePreparationCommand gamePreparationCommand = GamePreparationCommand.parseInput(command);
        cli.notify(gamePreparationCommand);

        cli.println("Wait for other players to place their Workers...");
    }
}
