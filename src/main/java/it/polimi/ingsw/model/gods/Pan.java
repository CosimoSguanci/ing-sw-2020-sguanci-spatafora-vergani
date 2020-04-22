package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;

import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the Pan strategy used by the Player who chose the powers of this God.
 * Specifically, Pan adds a Win Condition: the Player also win if the selected Worker moves
 * down two or more levels.
 *
 * @author Cosimo Sguanci
 */

public class Pan extends GodStrategy {

    public static final String NAME = "Pan";
    public static final String DESCRIPTION = "God of the Wild";
    public static final String POWER_DESCRIPTION = "Win Condition: You also win if your Worker moves down two or more levels.";

    @Override
    public Map<String, String> getGodInfo() {
        HashMap<String, String> info = new HashMap<>();
        info.put("name", NAME);
        info.put("description", DESCRIPTION);
        info.put("power_description", POWER_DESCRIPTION);
        return info;
    }

    /**
     * Flag used to determine if the Worker went down two or more levels.
     */
    private boolean downTwoLevels;


    /**
     * Implements standard execute worker movement, but if the level difference between
     * previous Cell and the new Cell is greater than or equal to 2, Pan's Win Condition
     * is triggered.
     *
     * @see GodStrategy#executeMove(Worker, Cell)
     * @param worker   the worker that the Player wants to move.
     * @param moveCell the cell in which the Player want to move the worker.
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
     * @see GodStrategy#checkWinCondition(Worker)
     * @param worker    the worker that the Player selected for this turn.
     * @return true if Pan Wind Conditions OR standard Win Conditions are satisfied, false otherwise.
     */
    @Override
    public boolean checkWinCondition(Worker worker) {
        return super.checkWinCondition(worker) || downTwoLevels;
    }
}
