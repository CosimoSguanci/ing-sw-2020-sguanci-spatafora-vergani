package it.polimi.ingsw.view.cli.components;

import it.polimi.ingsw.controller.GamePhase;
import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cli.Cli;

/**
 * This class manages changes in a single match.
 *
 * @author Andrea Vergani
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 */
public class OtherInfoHandler {
    private final Cli cli;

    private boolean soundPlayed = false;

    /**
     * This is the constructor of this class. At the moment of the creation
     * of a single instance of OtherInfoHandler, the cli associated to it is set
     *
     * @param cli contains reference to the Cli associated
     */
    public OtherInfoHandler(Cli cli) {
        this.cli = cli;
    }

    /**
     * This method handles tha changing in the turn.
     * Once a player ends his turn, current turn goes to the next player,
     * everyone is notified about the current turn. The current player
     * is also informed by a sound.
     */
    public void printCurrentTurn() {
        String currentPlayerNickname = this.cli.getController().getCurrentPlayerNickname();
        if (currentPlayerNickname != null && this.cli.playerWithColor(currentPlayerNickname) != null) {
            if (!this.cli.getController().getCurrentPlayerID().equals(this.cli.getController().getClientPlayerID())) {  //not player's turn
                soundPlayed = false;
                this.cli.println("It's " + this.cli.playerWithColor(currentPlayerNickname) + "'s turn!");
            } else {  //client's turn
                this.cli.println("It's" + Cli.convertColorToAnsi(this.cli.getPlayersColors().get(currentPlayerNickname)) + " your " + PrintableColor.RESET + "turn!");
                if (!soundPlayed) {
                    View.playOnTurnSound();
                    soundPlayed = true;
                }
            }
            this.cli.newLine();
        }
    }

    /**
     * This method is useful to advise players that everything is working.
     * They have just to wait for other player to complete the task requested
     * for a specific game phase
     *
     * @param currentGamePhase contains a reference to the new phase.
     *                         It is given from the updateHandler.
     */
    public void printGamePhase(GamePhase currentGamePhase) {
        switch (currentGamePhase) {
            case INITIAL_INFO:
                this.cli.println("Players are choosing nickname and color... Wait for your turn.");
                this.cli.newLine();
                break;
            case CHOOSE_GODS:
                this.cli.println("Players are choosing their gods... Wait for your turn.");
                this.cli.newLine();
                break;
            case GAME_PREPARATION:

                if (this.cli.getController().getCurrentPlayerID().equals(this.cli.getController().getClientPlayerID())) {
                    this.cli.printGamePreparationInfo();
                } else {
                    this.cli.println("Players are placing their Workers... Wait for your turn.");
                    this.cli.newLine();
                }

                break;
        }
    }
}
