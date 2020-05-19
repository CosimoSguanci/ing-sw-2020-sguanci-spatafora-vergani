package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;

public class TurnUpdate extends Update {
    public final String currentPlayerNickname;

    public TurnUpdate(String playerID, String playerNickname) {
        super(playerID, null);
        this.currentPlayerNickname = playerNickname;
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
