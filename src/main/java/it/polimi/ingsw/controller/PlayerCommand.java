package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;

public class PlayerCommand {

    final Player player;
    final CommandType commandType;
    final Worker worker;
    final Cell cell;


    public PlayerCommand(Player player, CommandType commandType, Worker worker, Cell cell) {
        this.player = player;
        this.commandType = commandType;
        this.worker = worker;
        this.cell = cell;
    }
}
