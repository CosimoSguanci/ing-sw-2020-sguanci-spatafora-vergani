package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;

/**
 * In this class there are references to the player who has playing
 * the current turn, the type of command requested from the player,
 * a reference to the worker the player wants it to move or build and
 * the cell in which the player wants the worker to move in or build in.
 */
public class PlayerCommand {

    final CommandType commandType;
    final Player player;
    final Worker worker;
    final Cell cell;
    final BlockType cellBlockType;

    /**
     * PlayerCommand is the builder of the class. Taken as parameters
     * the builder sets the class' attributes to the relatives values received.
     */
    public PlayerCommand(Player player, CommandType commandType, Worker worker, Cell cell, BlockType cellBlockType) {
        this.player = player;
        this.commandType = commandType;
        this.worker = worker;
        this.cell = cell;
        this.cellBlockType = cellBlockType;
    }
}
