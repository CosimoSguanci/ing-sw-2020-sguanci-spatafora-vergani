package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;

public class LoseUpdate extends BroadcastUpdate {

    public final String loserPlayerID;
    public final String loserPlayerNickname;

    public LoseUpdate(String loserPlayerID, String loserPlayerNickname, String board) {
        super(board);
        this.loserPlayerID = loserPlayerID;
        this.loserPlayerNickname = loserPlayerNickname;
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
