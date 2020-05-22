package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.UpdateHandler;

import java.io.Serializable;

public class ErrorUpdate extends Update {
    public final CommandType command;
    // todo add error type enum

    public ErrorUpdate(Player currentPlayer, CommandType command) {
        super(currentPlayer, null);
        this.command = command;
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
