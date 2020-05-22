package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.UpdateHandler;

public class WinUpdate extends Update {

    private final Player winnerPlayer;

    public WinUpdate(Player winnerPlayer) {
        super(null, null);
        this.winnerPlayer = winnerPlayer;
    }

    public Player getWinnerPlayer() {
        return winnerPlayer;
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
