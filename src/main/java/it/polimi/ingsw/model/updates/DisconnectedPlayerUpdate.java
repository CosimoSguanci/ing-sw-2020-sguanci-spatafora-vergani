package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;

public class DisconnectedPlayerUpdate extends BroadcastUpdate {

    public final String disconnectedPlayerID;
    public final String disconnectedPlayerNickname;

    /**
     * Flag which indicates if the only one player is still playing, in this case it automatically wins.
     */
    public final boolean onePlayerRemaining; // todo remove

    public DisconnectedPlayerUpdate(String disconnectedPlayerID, String disconnectedPlayerNickname, boolean onePlayerRemaining, String board) {
        super(board);
        this.disconnectedPlayerID = disconnectedPlayerID;
        this.disconnectedPlayerNickname = disconnectedPlayerNickname;
        this.onePlayerRemaining = onePlayerRemaining;
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
