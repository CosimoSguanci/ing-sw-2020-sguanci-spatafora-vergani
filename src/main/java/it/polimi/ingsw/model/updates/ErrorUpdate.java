package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.model.ErrorType;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.UpdateHandler;

import java.io.Serializable;
import java.util.Map;

public class ErrorUpdate extends Update {
    public final CommandType command;
    public final ErrorType errorType;
    private final Map<String, String> inhibitorGod; // [Optional] God who forbids the action attempted

    public ErrorUpdate(Player currentPlayer, CommandType command, ErrorType errorType, Map<String, String> inhibitorGod ) {
        super(currentPlayer, null);
        this.command = command;
        this.errorType = errorType;
        this.inhibitorGod = inhibitorGod;
    }

    public Map<String, String> getInhibitorGod() {
        return this.inhibitorGod;
    }


    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
