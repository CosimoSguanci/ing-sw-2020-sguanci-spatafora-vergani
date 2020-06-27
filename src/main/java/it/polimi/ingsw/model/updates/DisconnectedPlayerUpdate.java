package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.UpdateHandler;

/**
 * This class represents an update sent from Server to Client every time a Player disconnected.
 *
 * @author Cosimo Sguanci
 */
public class DisconnectedPlayerUpdate extends Update {
    /**
     * Reference to the Player that actually disconnected
     */
    private final Player disconnectedPlayer;

    public DisconnectedPlayerUpdate(Player disconnectedPlayer, String board) {
        super(board);
        this.disconnectedPlayer = disconnectedPlayer;
    }

    /**
     * Disconnected Player getter
     * @return disconnected Player
     */
    public Player getDisconnectedPlayer() {
        return this.disconnectedPlayer;
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
