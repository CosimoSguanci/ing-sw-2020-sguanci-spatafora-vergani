package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.view.UpdateHandler;

import java.io.Serializable;

public class ErrorUpdate extends PlayerSpecificUpdate {
    public final CommandType command;

    public ErrorUpdate(String playerID, CommandType command) {
        super(playerID);
        this.command = command;
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
