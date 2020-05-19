package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;

public class LoseUpdate extends Update {

    public final String loserPlayerID;
    public final String loserPlayerNickname; // todo consider make a superclasse for loseupdate and disconnectedplayerupdate

    /**
     * Flag which indicates if the only one player is still playing, in this case it automatically wins.
     */
    public final boolean onePlayerRemaining;

    public LoseUpdate(String loserPlayerID, String loserPlayerNickname, boolean onePlayerRemaining, String board) {
        super(null, board);
        this.loserPlayerID = loserPlayerID;
        this.loserPlayerNickname = loserPlayerNickname;
        this.onePlayerRemaining = onePlayerRemaining;
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
