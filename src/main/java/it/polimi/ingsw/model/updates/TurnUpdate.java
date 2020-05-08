package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;

public class TurnUpdate extends BroadcastUpdate {
    public final String currentPlayerID;
    public final String currentPlayerNickname;

    public TurnUpdate(String playerID, String playerNickname) {
        super(null);
        this.currentPlayerID = playerID;
        this.currentPlayerNickname = playerNickname;
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
