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

    public static final String NAME = "Athena";
    public static final String DESCRIPTION = "Godness of Wisdom";
    public static final String POWER_DESCRIPTION = "Opponent’s Turn: If one of your Workers moved up on your last turn, opponent Workers cannot move up this turn";

    /**
     * Flag representing the fact that Athena should use or not her power.
     */
    private boolean shouldBlockLevelUp;

    public Athena() {
        super(NAME, DESCRIPTION, POWER_DESCRIPTION);
    }


    /**
     * Implements movement execution by calling superclass standard executeMove,
     * setting the power flag if the God has moved up.
     *
     * @param worker   the worker that the Player wants to move.
     * @param moveCell the cell in which the Player want to move the worker.
     * @see GodStrategy#executeMove(Worker, Cell)
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
     * @param oppositeWorker the worker that the opposite Player wants to move.
     * @param moveCell       the cell in which the opposite Player want to move the worker.
     * @return true if the Move passed as parameter can be performed, false otherwise.
     */
    @Override
    public boolean checkMoveConstraints(Worker oppositeWorker, Cell moveCell) {
        return !shouldBlockLevelUp || moveCell.getLevel().getLevelNumber() <= oppositeWorker.getPosition().getLevel().getLevelNumber();
    }

    /**
     * This callback is called avery time a new turn starts (so the round of turns is over).
     * For Athena, this method reinitialize the flag which prevents other Players to move up.
     *
     * @param player The Player whose turn just started
     */
    @Override
    public void onTurnStarted(Player player) {
        shouldBlockLevelUp = false;
    }
}
