package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.UpdateHandler;

public class DisconnectedPlayerUpdate extends Update {

    private final Player disconnectedPlayer;

    /**
     * Flag which indicates if the only one player is still playing, in this case it automatically wins.
     */
    public final boolean onePlayerRemaining; // todo remove

    public DisconnectedPlayerUpdate(Player disconnectedPlayer, boolean onePlayerRemaining, String board) {
        super(null, board);
        this.disconnectedPlayer = disconnectedPlayer;
        this.onePlayerRemaining = onePlayerRemaining;
    }

    public Player getDisconnectedPlayer() {
        return this.disconnectedPlayer;
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
