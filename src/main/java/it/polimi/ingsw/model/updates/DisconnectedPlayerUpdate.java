package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.UpdateHandler;

public class DisconnectedPlayerUpdate extends Update {

    private final Player disconnectedPlayer;

    public DisconnectedPlayerUpdate(Player disconnectedPlayer, String board) {
        super(board);
        this.disconnectedPlayer = disconnectedPlayer;
    }

    public Player getDisconnectedPlayer() {
        return this.disconnectedPlayer;
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
