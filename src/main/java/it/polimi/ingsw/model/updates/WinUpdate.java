package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.UpdateHandler;

/**
 * This class represents an update sent from Server to Client when a Player wins the match.
 *
 * @author Cosimo Sguanci
 */
public class WinUpdate extends Update {

    /**
     * The Player who just won the match
     */
    private final Player winnerPlayer;

    public WinUpdate(Player winnerPlayer) {
        super( null);
        this.winnerPlayer = winnerPlayer;
    }

    /**
     * Winner Player getter
     * @return the player who just won
     */
    public Player getWinnerPlayer() {
        return winnerPlayer;
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
