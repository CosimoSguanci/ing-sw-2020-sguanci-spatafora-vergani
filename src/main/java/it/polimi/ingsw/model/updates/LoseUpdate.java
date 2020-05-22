package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.UpdateHandler;

public class LoseUpdate extends Update {

    public enum LoseCause {
        CANT_MOVE,
        CANT_BUILD,
    }

    private final Player loserPlayer;
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


    public Player getLoserPlayer() {
        return this.loserPlayer;
    }

    public LoseCause getLoseCause() {
        return this.loseCause;
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
