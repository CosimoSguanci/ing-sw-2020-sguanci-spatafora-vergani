package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;


/**
 * This class implements the Apollo strategy used by the Player who chose the powers of this God.
 * Specifically, Apollo allows the Worker to move to a Cell which is occupied by another player's worker,
 * swapping the worker's positions.
 *
 * @author Cosimo Sguanci
 */

public class Apollo extends GodStrategy {

    /**
     * Delegation pattern (composition over inheritance) is used to share common behaviours
     * between gods and to allow a single god to inherit multiple common properties.
     *
     * {@link OpponentWorkerMoverDelegate} is used by Apollo to swap positions with an opponent worker, if necessary.
     */
    private final OpponentWorkerMoverDelegate opponentWorkerMoverDelegate;

    public Apollo() {
        this.opponentWorkerMoverDelegate = new OpponentWorkerMoverDelegate();
    }

    /**
     * Implements standard controls on worker movement, except the check about emptiness of the new position
     * (Apollo player's worker can be swapped with an adjacent worker).
     *
     * @param worker   the worker that the Player wants to move.
     * @param moveCell the cell in which the Player want to move the worker.
     * @return true if the Move passed as parameter can be performed, false otherwise.
     */
    @Override
    public boolean checkMove(Worker worker, Cell moveCell) {
        return !worker.hasMoved() && !worker.hasBuilt() && worker.getPosition().isAdjacentTo(moveCell)
                && worker.getPosition().isLevelDifferenceOk(moveCell)
                && moveCell.getLevel() != BlockType.DOME;
    }

    /**
     * Implements standard execute worker movement, but if moveCell is not empty,
     * the opponent's worker that is occupying moveCell must be moved out of it,
     * and this is done delegating to {@link OpponentWorkerMoverDelegate}..
     *
     * @see GodStrategy#executeMove(Worker, Cell)
     * @see OpponentWorkerMoverDelegate#moveOpponentWorker(Worker, Cell)
     * @param worker   the worker that the Player wants to move.
     * @param moveCell the cell in which the Player want to move the worker.
     */
    @Override
    public void executeMove(Worker worker, Cell moveCell) {

        if (!moveCell.isEmpty()) {
            opponentWorkerMoverDelegate.moveOpponentWorker(moveCell.getWorker(), worker.getPosition());
        }

        super.executeMove(worker, moveCell);
    }

}
