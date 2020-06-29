package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.CommandHandler;
import it.polimi.ingsw.exceptions.BadCommandException;
import it.polimi.ingsw.model.Cell;

import java.util.Arrays;
import java.util.Map;


/**
 * GamePreparationCommand is the class that represents a {@link Command} specific for Game Preparation
 * phase. In particular, in this phase every player places his/her workers in two of the board's
 * cells.
 *
 * @author Andrea Mario Vergani
 * @author Cosimo Sguanci
 * @author Roberto Spatafora
 */
public class GamePreparationCommand extends Command {

    /**
     * "First" {@link it.polimi.ingsw.model.Worker} position (to be set by the {@link it.polimi.ingsw.controller.Controller} on server).
     */
    private transient Cell workerFirstCell;

    /**
     * "Second" {@link it.polimi.ingsw.model.Worker} position (to be set by the {@link it.polimi.ingsw.controller.Controller} on server).
     */
    private transient Cell workerSecondCell;

    /**
     * "First" {@link it.polimi.ingsw.model.Worker} row coordinate. Got from the command typed by the user.
     */
    public final int workerFirstRow;

    /**
     * "First" {@link it.polimi.ingsw.model.Worker} column coordinate. Got from the command typed by the user.
     */
    public final int workerFirstCol;

    /**
     * "Second" {@link it.polimi.ingsw.model.Worker} row coordinate. Got from the command typed by the user.
     */
    public final int workerSecondRow;

    /**
     * "Second" {@link it.polimi.ingsw.model.Worker} column coordinate. Got from the command typed by the user.
     */
    public final int workerSecondCol;

    /**
     * The constructor creates a {@link Command} specific for Game Preparation phase: {@link CommandType} is {@link CommandType#PLACE}
     * because this is what players must do in this phase; then, the positions of the workers are
     * selected.
     *
     * @param workerFirstRow  the row of the cell where the first worker must be placed
     * @param workerFirstCol  the column of the cell where the first worker must be placed
     * @param workerSecondRow the row of the cell where the second worker must be placed
     * @param workerSecondCol the column of the cell where the second worker must be placed
     */
    public GamePreparationCommand(int workerFirstRow, int workerFirstCol, int workerSecondRow, int workerSecondCol) {

        super(CommandType.PLACE);

        this.workerFirstRow = workerFirstRow;
        this.workerFirstCol = workerFirstCol;

        this.workerSecondRow = workerSecondRow;
        this.workerSecondCol = workerSecondCol;
    }


    /**
     * This method sets the cell where the first worker must be placed.
     *
     * @param workerFirstCell first worker's selected cell
     */
    public void setWorkerFirstCell(Cell workerFirstCell) {
        this.workerFirstCell = workerFirstCell;
    }

    /**
     * This method sets the cell where the second worker must be placed.
     *
     * @param workerSecondCell second worker's selected cell
     */
    public void setWorkerSecondCell(Cell workerSecondCell) {
        this.workerSecondCell = workerSecondCell;
    }

    /**
     * This method gets the cell where the first worker must be placed.
     *
     * @return first worker's selected cell
     */
    public Cell getWorkerFirstCell() {
        return workerFirstCell;
    }


    /**
     * This method gets the cell where the second worker must be placed.
     *
     * @return second worker's selected cell
     */
    public Cell getWorkerSecondCell() {
        return workerSecondCell;
    }


    /**
     * This method handles the GamePreparationCommand received by Server in the proper way.
     *
     * @param handler the object that is going to handle the command
     */
    @Override
    public void handleCommand(CommandHandler handler) {
        handler.handle(this);
    }


    /**
     * This static method parses a string to transform it into a valid GamePreparationCommand.
     * Command format is: CommandType (PLACE) + WorkerFirst + "BattleShip"-form cell for WorkerFirst +
     * WorkerSecond + "BattleShip"-form cell for WorkerSecond.
     *
     * @param command the string that must be converted to a valid GamePreparationCommand
     * @return a valid GamePreparationCommand, derived from the string passed as parameter
     * @throws BadCommandException if the string passed is not in the valid command format
     */
    public static GamePreparationCommand parseInput(String command) throws BadCommandException {
        try {

            String[] s;

            String[] initialString = command.split("\\s+");

            if (initialString[0].length() == 0) {  //case command starting with space
                s = Arrays.copyOfRange(initialString, 1, initialString.length);
            } else {
                s = Arrays.copyOf(initialString, initialString.length);
            }

            if (s.length != 5) {
                throw new BadCommandException();
            }

            s = Arrays.stream(s).map(String::toLowerCase).toArray(String[]::new);

            String type = s[0];

            CommandType commandType = CommandType.parseCommandType(type);

            if (commandType != CommandType.PLACE) {
                throw new BadCommandException();
            }


            String workerFirst = s[1];

            if (!workerFirst.equals(PlayerCommand.WORKER_FIRST)) {
                throw new BadCommandException();
            }

            String cellWorkerFirst = s[2];

            Map<String, Integer> workerFirstCellIdentifiers = Command.parseCellIdentifiers(cellWorkerFirst);

            String workerSecond = s[3];

            if (!workerSecond.equals(PlayerCommand.WORKER_SECOND)) {
                throw new BadCommandException();
            }

            String cellWorkerSecond = s[4];

            Map<String, Integer> workerSecondCellIdentifiers = Command.parseCellIdentifiers(cellWorkerSecond);


            if (workerFirstCellIdentifiers.get(ROW_KEY).equals(workerSecondCellIdentifiers.get(ROW_KEY))
                    && workerFirstCellIdentifiers.get(COL_KEY).equals(workerSecondCellIdentifiers.get(COL_KEY))) {
                throw new BadCommandException();
            }

            return new GamePreparationCommand(
                    workerFirstCellIdentifiers.get(ROW_KEY),
                    workerFirstCellIdentifiers.get(COL_KEY),
                    workerSecondCellIdentifiers.get(ROW_KEY),
                    workerSecondCellIdentifiers.get(COL_KEY)
            );
        } catch (Exception e) {
            throw new BadCommandException();
        }

    }
}
