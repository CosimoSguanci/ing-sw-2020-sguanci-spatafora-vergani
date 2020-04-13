package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

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



    public static PlayerCommand parseInput(Player player, String command) throws Exception{

        CommandType commandType;
        Worker worker;
        Cell cell;
        BlockType blockType = null;

        String[] s = command.split("\\s+");

        if(s.length > 4) {
            throw new Exception();
        }

        String type = s[0];

        switch(type.toLowerCase()) {
            case "move":
                commandType = CommandType.MOVE;
                break;
            case "build":
                commandType = CommandType.BUILD;
                break;
            case "end":
                commandType = CommandType.END_TURN;
                break;
            default:
                throw new Exception();
        }

        if(type.toLowerCase().equals("end")) {
            return new PlayerCommand(player, CommandType.END_TURN, null, null, null);
        }

        String workerStr = s[1];

        if(!workerStr.toLowerCase().equals("w1") && !workerStr.toLowerCase().equals("w2")) {
            throw new Exception();
        }

        String cellStr = s[2].toLowerCase();

        char rowChar = cellStr.charAt(0);
        int colNum = Integer.parseInt(cellStr.substring(1));

        if(cellStr.length() != 2 || colNum < 1 || colNum > Board.WIDTH_SIZE || rowChar < 'a' || rowChar > ('a' + Board.HEIGHT_SIZE)) {
            throw new Exception();
        }

        String blockTypeStr;
        if(s.length == 4) {
            blockTypeStr = s[3];

            switch(blockTypeStr) {
                case "one":
                    blockType = BlockType.LEVEL_ONE;
                    break;
                case "two":
                    blockType = BlockType.LEVEL_TWO;
                    break;
                case "three":
                    blockType = BlockType.LEVEL_THREE;
                    break;
                case "dome":
                    blockType = BlockType.DOME;
                    break;
                default:
                    throw new Exception();
            }
        }

        worker = workerStr.equals("w1") ? player.getWorkerFirst() : player.getWorkerSecond(); // JML
        cell = new Cell((int)rowChar - 'a', colNum - 1);

        return new PlayerCommand(player, commandType, worker, cell, blockType);






    }

    /*
        help -> print command format
        build w1/w2 [lettera, numero] [optional: blockType {one, two, three, dome}
        move  w1/w2 [lettera, numero]
        end
     */
}
