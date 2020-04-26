package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.controller.commands.CommandType;

import java.io.Serializable;

public class ErrorUpdate implements Serializable {
    public final String playerID; // TODO remove Player
    public final CommandType command;

    public ErrorUpdate(String playerID, CommandType command) {
        this.playerID = playerID;
        this.command = command;
    }
}
