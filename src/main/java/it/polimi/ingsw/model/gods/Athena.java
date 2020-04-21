package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;

import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the Athena strategy used by the Player who chose the powers of this God.
 * Specifically, if Athena has moved up, she doesn't allow other Player's workers to move up in the current turn.
 *
 * @author Cosimo Sguanci
 */

public class Athena extends GodStrategy {

    public static final String NAME = "Athena";
    public static final String DESCRIPTION = "Description";
    public static final String POWER_DESCRIPTION = "Power Description";

    /**
     * Flag representing the fact that Athena should use or not her power.
     */
    private boolean shouldBlockLevelUp;

    @Override
    public Map<String, String> getGodInfo() {
        HashMap<String, String> info = new HashMap<>();
        info.put("name", NAME);
        info.put("description", DESCRIPTION);
        info.put("power_description", POWER_DESCRIPTION);
        return info;
    }

    /**
     * Implements movement execution by calling superclass standard executeMove,
     * setting the power flag if the God has moved up.
     *
     * @see GodStrategy#executeMove(Worker, Cell)
     * @param worker   the worker that the Player wants to move.
     * @param moveCell the cell in which the Player want to move the worker.
     */
    @Override
    public void executeMove(Worker worker, Cell moveCell) {
        Cell prev = worker.getPosition();
        super.executeMove(worker, moveCell);
        shouldBlockLevelUp = (moveCell.levelDifference(prev) >= 1);
    }

    /**
     * Implements movement constraints to other Player's workers.
     * If Athena as moved up, none of the others Players can move up.
     *
     * @param oppositeWorker    the worker that the opposite Player wants to move.
     * @param moveCell          the cell in which the opposite Player want to move the worker.
     * @return true if the Move passed as parameter can be performed, false otherwise.
     */
    @Override
    public boolean checkMoveConstraints(Worker oppositeWorker, Cell moveCell) {
        //return !(shouldBlockLevelUp && moveCell.getLevel().getLevelNumber() > oppositeWorker.getPosition().getLevel().getLevelNumber());
        return !shouldBlockLevelUp || moveCell.getLevel().getLevelNumber() <= oppositeWorker.getPosition().getLevel().getLevelNumber();
    }

    /**
     * This method calls superclass endTurn, then resets move constraint flag.
     *
     * @see GodStrategy#endTurn(Player)
     * @param player    Player corresponding to the current turn.
     */
    @Override
    public void endTurn(Player player) {
        super.endTurn(player);
        shouldBlockLevelUp = false;
    }
}
