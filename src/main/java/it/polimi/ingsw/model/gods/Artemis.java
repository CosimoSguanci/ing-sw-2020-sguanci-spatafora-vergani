package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;

/**
 * This class implements the Artemis strategy used by the Player who chose the powers of this God.
 * Specifically, Artemis allows the Worker to move one additional time using {@link MultipleMovementDelegate}, but not back to
 * its initial position, which is saved using {@link PreviousCellNeededDelegate}.
 *
 * @author Cosimo Sguanci
 */

public class Artemis extends GodStrategy {

    /**
     * Constant representing the max number of times this God can move.
     */
    final int ARTEMIS_MAX_MOVE_NUM = 2;
    private MultipleMovementDelegate multipleMovementDelegate;
    private PreviousCellNeededDelegate previousCellNeededDelegate;

    public Artemis() {
        this.multipleMovementDelegate = new MultipleMovementDelegate(ARTEMIS_MAX_MOVE_NUM);
        this.previousCellNeededDelegate = new PreviousCellNeededDelegate();
    }

    /**
     * Implements movements check by delegating to {@link MultipleMovementDelegate}
     * and checking that moveCell is not equal to the original Worker position (in this turn).
     *
     * @see MultipleMovementDelegate#checkMove(Worker, Cell, Worker) 
     * @param worker   the worker that the Player wants to move.
     * @param moveCell the cell in which the Player want to move the worker.
     * @return true if the Move passed as parameter can be performed, false otherwise.
     */
    @Override
    public boolean checkMove(Worker worker, Cell moveCell) {
        return multipleMovementDelegate.checkMove(worker, moveCell, selectedWorker) && !(moveCell.equals(previousCellNeededDelegate.getPreviousCell()));
    }

    /**
     * This method first saves the previous position using {@link PreviousCellNeededDelegate} in order to check that the Worker
     * doesn't move an additional time to its initial position, then it executes standard move, finally movement count of
     * {@link MultipleMovementDelegate} is increased.
     *
     * @see GodStrategy#executeMove(Worker, Cell)
     * @param worker   the worker that the Player wants to move.
     * @param moveCell the cell in which the Player want to move the worker.
     */
    @Override
    public void executeMove(Worker worker, Cell moveCell) {
        previousCellNeededDelegate.setPreviousCell(worker.getPosition());
        super.executeMove(worker, moveCell);
        multipleMovementDelegate.increaseMoveCount();
    }

    /**
     * This method calls superclass endTurn, then resets movement count and previous cell at the end of Player's turn.
     *
     * @see GodStrategy#endTurn(Player)
     * @param player    Player corresponding to the current turn.
     */
    @Override
    public void endTurn(Player player) {
        super.endTurn(player);
        multipleMovementDelegate.reinitializeMoveCount();
        previousCellNeededDelegate.reinitializeCell();
    }
}
