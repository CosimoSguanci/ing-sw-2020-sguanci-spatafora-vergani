package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;

/**
 * This class implements the Pan strategy used by the {@link Player} who chose the powers of this God.
 * Specifically, Pan adds a Win Condition: the {@link Player} also win if the selected {@link Worker} moves
 * down two or more levels.
 *
 * @author Cosimo Sguanci
 */

public class Pan extends GodStrategy {

    public static final String NAME = "Pan";
    public static final String DESCRIPTION = "God of the Wild";
    public static final String POWER_DESCRIPTION = "Win Condition: You also win if your Worker moves down two or more levels";

    /**
     * Flag used to determine if the Worker went down two or more levels.
     */
    private boolean downTwoLevels;

    public Pan() {
        super(NAME, DESCRIPTION, POWER_DESCRIPTION);
    }

    /**
     * Implements standard execute worker movement, but if the level difference between
     * previous Cell and the new Cell is greater than or equal to 2, Pan's Win Condition
     * is triggered.
     *
     * @param worker   the worker that the Player wants to move.
     * @param moveCell the cell in which the Player want to move the worker.
     * @see GodStrategy#executeMove(Worker, Cell)
     */
    @Override
    public void executeMove(Worker worker, Cell moveCell) {
        Cell prev = worker.getPosition();
        super.executeMove(worker, moveCell);
        downTwoLevels = prev.levelDifference(moveCell) >= 2;
    }

    /**
     * This method checks if standard Win Conditions or Pan Win Conditions are satisfied.
     *
     * @param worker the worker that the Player selected for this turn.
     * @return true if Pan Wind Conditions OR standard Win Conditions are satisfied, false otherwise.
     * @see GodStrategy#checkWinCondition(Worker)
     */
    @Override
    public boolean checkWinCondition(Worker worker) {
        return super.checkWinCondition(worker) || downTwoLevels;
    }
}
