package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;

public class PlayerCommand {

    final CommandType commandType;
    final Player player;
    final Worker worker;
    final Cell cell;
    final BlockType cellBlockType;


    public PlayerCommand(Player player, CommandType commandType, Worker worker, Cell cell, BlockType cellBlockType) {
        this.commandType = commandType;
        this.player = player;
        this.worker = worker;
        this.cell = cell;
        this.cellBlockType = cellBlockType;
    }
}
