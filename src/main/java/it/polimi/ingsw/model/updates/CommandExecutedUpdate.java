package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;

public class CommandExecutedUpdate extends BroadcastUpdate {

    public CommandExecutedUpdate(String board) {
        super(board);
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(new BoardUpdate(null));
    }
}
