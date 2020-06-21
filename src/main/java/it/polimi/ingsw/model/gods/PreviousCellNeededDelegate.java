package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.Cell;

/**
 * This class represent the delegate that Gods can use if their power needs to save any previous position of a worker.
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

class PreviousCellNeededDelegate {
    private Cell previousCell;

    /**
     * Previous Cell getter
     */
    Cell getPreviousCell() {
        return this.previousCell;
    }

    /**
     * Previous Cell setter
     */
    void setPreviousCell(Cell cell) {
        this.previousCell = cell;
    }

    /**
     * Method used to check if previousCell has been set or not.
     *
     * @return true if and only if the previous cell is not null
     */
    boolean hasPreviousCell() {
        return this.previousCell != null;
    }

    /**
     * Reinitialize previousCell to null
     */
    void reinitializeCell() {
        this.previousCell = null;
    }
}
