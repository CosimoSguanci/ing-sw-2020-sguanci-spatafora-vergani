package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;

/**
 * This class implements the Athena strategy used by the Player who chose the powers of this God.
 * Specifically, if Athena has moved up, she doesn't allow other Player's workers to move up in the current turn.
 *
 * @author Cosimo Sguanci
 */

public class Athena extends GodStrategy {

    /**
     * Flag representing the fact that Athena should use or not her power.
     */
    private boolean shouldBlockLevelUp;

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
        shouldBlockLevelUp = (moveCell.levelDifference(prev) == 1);
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
        return !shouldBlockLevelUp || oppositeWorker.getPosition().getLevel().getLevelNumber() < moveCell.getLevel().getLevelNumber();
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
