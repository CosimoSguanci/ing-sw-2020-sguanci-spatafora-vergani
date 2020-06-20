package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.CommandHandler;
import it.polimi.ingsw.exceptions.BadCommandException;
import it.polimi.ingsw.model.*;

import java.util.Arrays;
import java.util.Map;

/**
 * In this class there are references to the player who is playing
 * the current turn during the RealGame phase, the type of command requested by the player,
 * the worker he/she wants to move or build with and
 * the cell in which the player wants to move or build in.
 *
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 */

public class PlayerCommand extends Command {

    public final String workerID;

    /**
     * The Row/Column identifier that is used to identify the Cell in which the Player wants to move his/her Worker,
     * or where the Worker wants to build a new level.
     */
    public final int row;
    public final int col;

    public final BlockType cellBlockType;

    /**
     * This properties must be reconstructed by server-side Controller
     */
    private transient Worker worker;
    private transient Cell cell;


    /**
     * PlayerCommand is the constructor of the class. It sets the class' attributes
     * such as the commandType (move, build, ...), the interested worker, the destination-cell
     * of move/build and an optional level (in case of building a block).
     *
     * @param commandType the type of command, specific of RealGame phase
     * @param workerID ID of worker to move/build with; null if the operation involves no
     *                 worker (such as an end of turn)
     * @param row an integer representing the row of cell where to move/build in; possible
     *            values are from 0 to number of rows in Board minus one; -1 if the operation
     *            involves no cell (such as an end of turn)
     * @param col an integer representing the column of cell where to move/build in; possible
     *            values are from 0 to number of columns in Board minus one; -1 if the
     *            operation involves no cell (such as an end of turn)
     * @param cellBlockType the type of block that the player wants to build; null if the
     *                      operation is not a build, or the player wants to build the default
     *                      level (so he/she wants to build just one level higher)
     */
    public PlayerCommand(CommandType commandType, String workerID, int row, int col, BlockType cellBlockType) {
        super(commandType);
        this.workerID = workerID;
        this.row = row;
        this.col = col;
        this.cellBlockType = cellBlockType;
    }


    /**
     * This method is the setter for "cell" property, that must be reconstructed by server-side
     * Controller.
     *
     * @param cell the cell where to move/build in
     */
    public void setCell(Cell cell) {
        this.cell = cell;
    }


    /**
     * This method is the getter for "cell" property, that is reconstructed by server-side
     * Controller.
     *
     * @return the cell where to move/build in
     */
    public Cell getCell() {
        return this.cell;
    }


    /**
     * This method is the getter for "worker" property, that is reconstructed by server-side
     * Controller.
     *
     * @return the worker that is going to move/build
     */
    public Worker getWorker() {
        return this.worker;
    }


    /**
     * This method is the setter for "worker" property, that must be reconstructed by server-side
     * Controller.
     *
     * @param worker the worker that is going to move/build
     */
    public void setWorker(Worker worker) {
        this.worker = worker;
    }


    /**
     * This method handles the PlayerCommand received by Server in the proper way.
     *
     * @param handler the object that is going to handle the command
     */
    @Override
    public void handleCommand(CommandHandler handler) {
        handler.handle(this);
    }

    /**
     * Static method used to parse a String command input by the user (CLI) into a PlayerCommand instance using String-rep for Workers.
     *
     * @param command  input String taken from Standard Input
     * @return the corresponding PlayerCommand instance (if the command is well formatted)
     * @throws BadCommandException if the command String is not well formatted
     */
    public static PlayerCommand parseInput(String command) throws BadCommandException {

        try {
            CommandType commandType;
            BlockType blockType = null;
            String[] s;

            String[] initialString = command.split("\\s+");

            if (initialString[0].length() == 0) {  // command starting with space
                s = Arrays.copyOfRange(initialString, 1, initialString.length);
            } else {
                s = Arrays.copyOf(initialString, initialString.length);
            }

            if (s.length > 4) {
                throw new BadCommandException();
            }

            s = Arrays.stream(s).map(String::toLowerCase).toArray(String[]::new);

            String type = s[0];

            commandType = CommandType.parseCommandType(type);

            if (commandType == CommandType.END_TURN) {
                if (s.length == 1)
                    return new PlayerCommand(CommandType.END_TURN, null, -1, -1, null);
                else
                    throw new BadCommandException();
            }

            String worker = s[1];

            if (!worker.equals(WORKER_FIRST) && !worker.equals(WORKER_SECOND)) {
                throw new BadCommandException();
            }

            Map<String, Integer> cellIdentifiers = Command.parseCellIdentifiers(s[2]);

            String blockTypeStr;
            if (s.length == 4) {
                if (commandType != CommandType.BUILD) {
                    throw new BadCommandException();
                } else {
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
                            throw new BadCommandException();
                    }
                }
            }

            return new PlayerCommand(commandType, worker, cellIdentifiers.get(ROW_KEY), cellIdentifiers.get(COL_KEY), blockType);
        } catch (Exception e) {
            throw new BadCommandException();
        }
    }
}
