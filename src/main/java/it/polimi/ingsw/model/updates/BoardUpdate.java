package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.controller.commands.PlayerCommand;
import it.polimi.ingsw.view.UpdateHandler;

public class BoardUpdate extends BroadcastUpdate {

    private PlayerCommand executedCommand;

    public BoardUpdate(String board) {
        super(board);
    }

    public void setExecutedCommand(PlayerCommand executedCommand) {
        this.executedCommand = executedCommand;
    }

    public PlayerCommand getExecutedCommand() {
        return this.executedCommand;
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
