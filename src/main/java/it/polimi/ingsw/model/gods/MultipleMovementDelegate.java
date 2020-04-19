package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;

/**
 * This class represent the delegate that Gods can use if their power provides the possibility to move
 * more than one time each turn.
 *
 * The use of this kind of delegates allows the application of the Composition over Inheritance principle,
 * making it easy to have Gods that are not strictly categorized and that can instead share and use features which are
 * in common with other Gods, in order to also reduce redundancy and code duplication. Moreover, using Delegates instead of
 * Interfaces allows not only to implement non-static shared methods and centralized state management, but also to avoid the
 * exposure of some methods which should only be used by Gods, and not visible from outside.
 * For this purpose, Delegates class and methods are package-level visibility, while attributes are private to the Delegate.
 *
 * @author Cosimo Sguanci
 */

class MultipleMovementDelegate {
    private final int MAX_MOVE_COUNT;
    private int moveCount;

    MultipleMovementDelegate(int maxMoveCount) {
        this.MAX_MOVE_COUNT = maxMoveCount;
    }

    void increaseMoveCount() {this.moveCount++;}
    void reinitializeMoveCount(){this.moveCount = 0;}
    boolean canMoveAgain() {
        return moveCount < MAX_MOVE_COUNT;
    }

    /**
     * This method is used to check if the move action that Player attempted can be performed. It is necessary because the Gods which have
     * multiple movement power cannot use {@link GodStrategy} checkMove, because it always return false if the Worker has already moved one time
     * (following the standard game rules).
     *
     * @param worker            the worker that the Player wants to move.
     * @param moveCell          the cell in which the Player want to move the worker.
     * @param selectedWorker    to check if the worker that is trying to move is the same worker that performed movement before in the current turn.
     * @return true if the moveCount is less than the max number of times that the God can build each turn and other standard check are satisfied, false otherwise.
     */
    boolean checkMove(Worker worker, Cell moveCell, Worker selectedWorker) {
        if(moveCount > 0 && !worker.equals(selectedWorker)) {
            return false;
        }

        if (canMoveAgain() && !worker.hasBuilt() && worker.getPosition().isAdjacentTo(moveCell) && (moveCell.isEmpty()) && (moveCell.getLevel() != BlockType.DOME)) {

                if (worker.getPosition().isLevelDifferenceOk(moveCell)) { // Moving into a DOME is not allowed.
                    return true;
                }
                //else {throw new Exception(); } //else { throw new InvalidMoveException(); }

            //else { throw new Exception(); } //else { throw new WorkerCannotMoveUpException(); }
        }
        //else {throw new Exception(); }

        return false;
    }
}
