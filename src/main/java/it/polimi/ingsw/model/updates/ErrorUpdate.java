package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.controller.commands.CommandType;

import java.io.Serializable;

public class ErrorUpdate extends PlayerSpecificUpdate {
    public final CommandType command;

    public ErrorUpdate(String playerID, CommandType command) {
        super(playerID);
        this.command = command;
    }
}
