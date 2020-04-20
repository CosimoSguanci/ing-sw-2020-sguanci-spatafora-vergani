package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

/**
 * In this class there are references to the player who has playing
 * the current turn, the type of command requested from the player,
 * a reference to the worker the player wants it to move or build and
 * the cell in which the player wants the worker to move in or build in.
 *
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 */

public class PlayerCommand {

    /**
     * Properties used by the Client to construct a PlayerCommand without the need to have the actual reference to Player and Worker instances.
     */
    final static String WORKER_FIRST = "w1";
    final static String WORKER_SECOND = "w2";
    final String playerID;
    final String workerID;

    final CommandType commandType;
    final BlockType cellBlockType;

    /**
     * This properties must be reconstructed by server-side Controller
     */
    private Player player;
    private Worker worker;
    private Cell cell;

    /**
     * PlayerCommand is the builder of the class. Taken as parameters
     * the builder sets the class' attributes to the relatives values received.
     */
    public PlayerCommand(String playerID, CommandType commandType, String workerID, Cell cell, BlockType cellBlockType) {
        this.playerID = playerID;
        this.commandType = commandType;
        this.workerID = workerID;
        this.cell = cell;
        this.cellBlockType = cellBlockType;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public Cell getCell() {
        return cell;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }


    /**
     * Static method used to parse a String command input by the user (CLI) into a PlayerCommand instance using String-rep for Workers.
     *
     * @param playerID  the ID of the Player who launched the command
     * @param command   input String taken from Standard Input
     * @return the corresponding PlayerCommand instance if the command was well formatted.
     * @throws Exception if the command String isn't well formatted.
     */
    public static PlayerCommand parseInput(String playerID, String command) throws Exception {

        CommandType commandType;
        Cell cell;
        BlockType blockType = null;

        String[] s = command.split("\\s+");

        if (s.length > 4) {
            throw new Exception();
        }

        String type = s[0];

        switch (type.toLowerCase()) {
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

        if (type.toLowerCase().equals("end")) {
            if(s.length == 1)
                return new PlayerCommand(playerID, CommandType.END_TURN, null, null, null);
            else
                throw new Exception();
        }

        String worker = s[1];

        if (!worker.toLowerCase().equals(WORKER_FIRST) && !worker.toLowerCase().equals(WORKER_SECOND)) {
            throw new Exception();
        }

        String cellStr = s[2].toLowerCase();

        char rowChar = cellStr.charAt(0);
        int colNum = Integer.parseInt(cellStr.substring(1));

        if (cellStr.length() != 2 || colNum < 1 || colNum > Board.WIDTH_SIZE || rowChar < 'a' || rowChar > ('a' + Board.HEIGHT_SIZE)) {
            throw new Exception();
        }

        String blockTypeStr;
        if (s.length == 4) {
            blockTypeStr = s[3];

            switch (blockTypeStr) {
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


        cell = new Cell((int) rowChar - 'a', colNum - 1);
        return new PlayerCommand(playerID, commandType, worker, cell, blockType);
    }

    /*
        help -> print command format
        build w1/w2 [lettera, numero] [optional: blockType {one, two, three, dome}
        move  w1/w2 [lettera, numero]
        end
     */
}
