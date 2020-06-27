package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.UpdateHandler;

/**
 * This class represents an update sent from Server to Client every time a Player loses.
 *
 * @author Cosimo Sguanci
 */
public class LoseUpdate extends Update {

    /**
     * Enum representing what made the Player lose, in this case a Player
     * can lose if it can't move when its turn starts, or if it can't build after a move command.
     */
    public enum LoseCause {
        CANT_MOVE,
        CANT_BUILD,
    }

    /**
     * The Player who lost
     */
    private final Player loserPlayer;

    /**
     * What caused the Player to lost
     */
    private final LoseCause loseCause;

    /**
     * Flag which indicates if the only one player is still playing, in this case it automatically wins.
     */
    public final boolean onePlayerRemaining;

    public LoseUpdate(Player loserPlayer, LoseCause loseCause, boolean onePlayerRemaining, String board) {
        super(board);
        this.loserPlayer = loserPlayer;
        this.onePlayerRemaining = onePlayerRemaining;
        this.loseCause = loseCause;
    }

    /**
     * Loser Player Getter
     * @return the Player who just lost
     */
    public Player getLoserPlayer() {
        return this.loserPlayer;
    }

    /**
     * Lose Cause Getter
     * @return what caused the Player to lost
     */
    public LoseCause getLoseCause() {
        return this.loseCause;
    }

    /**
     * Utility method used to implement Visitor Pattern for Updates handling.
     * @see UpdateHandler
     * @param handler which handle the updates to update the View
     */
    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
