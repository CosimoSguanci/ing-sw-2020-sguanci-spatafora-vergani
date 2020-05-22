package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.UpdateHandler;

public class LoseUpdate extends Update {

    private final Player loserPlayer;

    /**
     * Flag which indicates if the only one player is still playing, in this case it automatically wins.
     */
    public final boolean onePlayerRemaining;

    public LoseUpdate(Player loserPlayer, boolean onePlayerRemaining, String board) {
        super(null, board);
        this.loserPlayer = loserPlayer;
        this.onePlayerRemaining = onePlayerRemaining;
    }


    public Player getLoserPlayer() {
        return this.loserPlayer;
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
