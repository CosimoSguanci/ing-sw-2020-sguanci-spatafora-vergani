package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;


/**
 * This class implements the Artemis strategy used by the Player who chose the powers of this God.
 * Specifically, Artemis allows the Worker to move one additional time using {@link MultipleMovementDelegate}, but not back to
 * its initial position, which is saved using {@link PreviousCellNeededDelegate}.
 *
 * @author Cosimo Sguanci
 */

public class Artemis extends GodStrategy {

    public static final String NAME = "Artemis";
    public static final String DESCRIPTION = "Goddess of the Hunt";
    public static final String POWER_DESCRIPTION = "Your Move: Your Worker may move one additional time, but not back to its initial space";

    /**
     * Constant representing the max number of times this God can move.
     */
    final int ARTEMIS_MAX_MOVE_NUM = 2;
    private final MultipleMovementDelegate multipleMovementDelegate;
    private final PreviousCellNeededDelegate previousCellNeededDelegate;


    public Artemis() {
        super(NAME, DESCRIPTION, POWER_DESCRIPTION);
        this.multipleMovementDelegate = new MultipleMovementDelegate(ARTEMIS_MAX_MOVE_NUM);
        this.previousCellNeededDelegate = new PreviousCellNeededDelegate();
    }

    /**
     * Implements movements check by delegating to {@link MultipleMovementDelegate}
     * and checking that moveCell is not equal to the original Worker position (in this turn).
     *
     * @param worker   the worker that the Player wants to move.
     * @param moveCell the cell in which the Player want to move the worker.
     * @return true if the Move passed as parameter can be performed, false otherwise.
     * @see MultipleMovementDelegate#checkMove(Worker, Cell, Worker)
     */
    @Override
    public boolean checkMove(Worker worker, Cell moveCell) {
        return multipleMovementDelegate.checkMove(worker, moveCell, selectedWorker) &&
                (!previousCellNeededDelegate.hasPreviousCell() || !previousCellNeededDelegate.getPreviousCell().equals(moveCell));
    }

    /**
     * This method first saves the previous position using {@link PreviousCellNeededDelegate} in order to check that the Worker
     * doesn't move an additional time to its initial position, then it executes standard move, finally movement count of
     * {@link MultipleMovementDelegate} is increased.
     *
     * @param worker   the worker that the Player wants to move.
     * @param moveCell the cell in which the Player want to move the worker.
     * @see GodStrategy#executeMove(Worker, Cell)
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
     * @param player Player corresponding to the current turn.
     * @see GodStrategy#endPlayerTurn(Player)
     */
    @Override
    public void endPlayerTurn(Player player) {
        super.endPlayerTurn(player);
        multipleMovementDelegate.reinitializeMoveCount();
        previousCellNeededDelegate.reinitializeCell();
    }

    /**
     * This method calls superclass canBuild, if it returns false, the delegate canMove method is called,
     * to determine if the Player has lost or if it can still perform another move.
     *
     * @param board the Match board to consider
     * @param worker the worker whose ability to build is about to be tested
     * @return true if the Worker passed can build, or if it can move again (and maybe build from the new position), false otherwise.
     * @see GodStrategy#canBuild(Board, Worker)
     */
    @Override
    public boolean canBuild(Board board, Worker worker) {
        if (super.canBuild(board, worker))
            return true;

        if (!super.canBuild(board, worker) && !multipleMovementDelegate.canMoveAgain()) {
            return false;
        }

        return false;
    }
}
