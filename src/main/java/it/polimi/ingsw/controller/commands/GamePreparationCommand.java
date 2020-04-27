package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.CommandHandler;
import it.polimi.ingsw.exceptions.BadCommandException;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.UpdateHandler;

import java.util.Arrays;
import java.util.Map;

public class GamePreparationCommand extends Command {


    private transient Cell workerFirstCell;
    private transient Cell workerSecondCell;

    public final int workerFirstRow;
    public final int workerFirstCol;

    public final int workerSecondRow;
    public final int workerSecondCol;

    public GamePreparationCommand(int workerFirstRow, int workerFirstCol, int workerSecondRow, int workerSecondCol) {

        this.workerFirstRow = workerFirstRow;
        this.workerFirstCol = workerFirstCol;

        this.workerSecondRow = workerSecondRow;
        this.workerSecondCol = workerSecondCol;
    }

    public void setWorkerFirstCell(Cell workerFirstCell) {
        this.workerFirstCell = workerFirstCell;
    }

    public void setWorkerSecondCell(Cell workerSecondCell) {
        this.workerSecondCell = workerSecondCell;
    }

    public Cell getWorkerFirstCell() {
        return workerFirstCell;
    }

    public Cell getWorkerSecondCell() {
        return workerSecondCell;
    }

    @Override
    public void handleCommand(CommandHandler handler) {
        handler.handle(this);
    }

    public static GamePreparationCommand parseInput(String command) throws BadCommandException {
        // place w1 c1 w2 a2
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

            s = Arrays.stream(s).map(String::toLowerCase).toArray(String[]::new); // TODO Test

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
