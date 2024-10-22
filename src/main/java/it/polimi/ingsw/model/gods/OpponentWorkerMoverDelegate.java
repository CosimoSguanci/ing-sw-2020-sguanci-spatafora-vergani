package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;

/**
 * This class represent the delegate that Gods can use if their power provides the possibility to move an opponent Worker.
 * <p>
 * The use of this kind of delegates allows the application of the Composition over Inheritance principle,
 * making it easy to have Gods that are not strictly categorized and that can instead share and use features which are
 * in common with other Gods, in order to also reduce redundancy and code duplication. Moreover, using Delegates instead of
 * Interfaces allows not only to implement non-static shared methods and centralized state management, but also to avoid the
 * exposure of some methods which should only be used by Gods, and not visible from outside.
 * For this purpose, Delegates class and methods are package-level visibility, while attributes are private to the Delegate.
 *
 * @author Cosimo Sguanci
 */

class OpponentWorkerMoverDelegate {

    /**
     * Simply calls the opponent {@link Worker} move method to change its position to the forced one passed as parameter
     *
     * @param opponent            opponent Worker that has to be moved
     * @param opponentNewPosition new opponent Worker position
     */
    void moveOpponentWorker(Worker opponent, Cell opponentNewPosition) {
        opponent.move(opponentNewPosition);
        opponent.reinitializeBuiltMoved();
    }

    /**
     * This method is used to check if moveCell is occupied by the other {@link Worker} of the {@link Player}.
     *
     * @param worker   Player's Worker that is about to move
     * @param moveCell new Worker position
     * @return true if and only if moveCell is not occupied by the other Player's Worker.
     */
    boolean isNotOtherPlayerWorkerPosition(Worker worker, Cell moveCell) {
        Worker otherWorker = worker.equals(worker.player.getWorkerFirst()) ? worker.player.getWorkerSecond() : worker.player.getWorkerFirst();
        return !moveCell.getWorker().equals(otherWorker);
    }
}
