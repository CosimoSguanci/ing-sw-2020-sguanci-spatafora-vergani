package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;

public class LoseUpdate extends BroadcastUpdate {

    public final String loserPlayerID;
    public final String loserPlayerNickname;

    /**
     * Flag which indicates if the only one player is still playing, in this case it automatically wins.
     */
    public final boolean onePlayerRemaining;

    public LoseUpdate(String loserPlayerID, String loserPlayerNickname, boolean onePlayerRemaining, String board) {
        super(board);
        this.loserPlayerID = loserPlayerID;
        this.loserPlayerNickname = loserPlayerNickname;
        this.onePlayerRemaining = onePlayerRemaining;
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
