package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;

public class WinUpdate extends Update {

    public final String winnerPlayerID;
    public final String winnerPlayerNickname;

    public WinUpdate(String winnerPlayerID, String winnerPlayerNickname) {
        super(null, null);
        this.winnerPlayerID = winnerPlayerID;
        this.winnerPlayerNickname = winnerPlayerNickname;
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
