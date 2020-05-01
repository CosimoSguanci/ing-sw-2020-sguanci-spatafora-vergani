package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.CommandHandler;
import it.polimi.ingsw.exceptions.BadCommandException;
import it.polimi.ingsw.model.*;

import java.util.Arrays;
import java.util.Map;

/**
 * In this class there are references to the player who has playing
 * the current turn, the type of command requested from the player,
 * a reference to the worker the player wants it to move or build and
 * the cell in which the player wants the worker to move in or build in.
 *
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 */

public class PlayerCommand extends Command {

    public final String workerID;

    /**
     * The Row/Column identifier that's used to identify the Cell in which the Player want to move its Worker,
     * or where the Worker wants to build a new level.
     */
    public final int row;
    public final int col;

    public final CommandType commandType;
    public final BlockType cellBlockType;

    /**
     * This properties must be reconstructed by server-side Controller
     */
    private transient Worker worker;
    private transient Cell cell;


    /**
     * PlayerCommand is the builder of the class. Taken as parameters
     * the builder sets the class' attributes to the relatives values received.
     */
    public PlayerCommand(CommandType commandType, String workerID, int row, int col, BlockType cellBlockType) {
        super(commandType);
        this.commandType = commandType;
        this.workerID = workerID;
        this.row = row;
        this.col = col;
        this.cellBlockType = cellBlockType;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public Cell getCell() {
        return this.cell;
    }

    public Worker getWorker() {
        return this.worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    @Override
    public void handleCommand(CommandHandler handler) {
        handler.handle(this);
    }

    /**
     * Static method used to parse a String command input by the user (CLI) into a PlayerCommand instance using String-rep for Workers.
     *
     * @param command  input String taken from Standard Input
     * @return the corresponding PlayerCommand instance if the command was well formatted.
     * @throws BadCommandException if the command String isn't well formatted.
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

            s = Arrays.stream(s).map(String::toLowerCase).toArray(String[]::new); // TODO Test

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
