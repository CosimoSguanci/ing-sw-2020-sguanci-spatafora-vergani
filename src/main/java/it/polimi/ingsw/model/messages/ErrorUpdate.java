package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.controller.CommandType;
import it.polimi.ingsw.controller.PlayerCommand;
import it.polimi.ingsw.model.Player;

public class ErrorUpdate {
    private Player player;
    private CommandType command;

    public ErrorUpdate(Player player, CommandType command) {
        this.player = player;
        this.command = command;
    }

    public CommandType getCommand() {
        return command;
    }

    public Player getPlayer() {
        return player;
    }
}
