package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;

/**
 * This class represent the delegate that Gods can use if their power provides the possibility to build
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

class MultipleBuildDelegate {
    private final int MAX_BUILD_COUNT;
    private int buildCount;

    MultipleBuildDelegate(int maxBuildCount) {
        this.MAX_BUILD_COUNT = maxBuildCount;
    }

    int getBuildCount() {
        return buildCount;
    }
    void increaseBuildCount() {this.buildCount++;}
    void reinitializeBuildCount(){this.buildCount = 0;}
    boolean canBuildAgain() {
        return buildCount < MAX_BUILD_COUNT;
    }

    /**
     * This method is used to check if the build action that Player attempted can be performed. It is necessary because the Gods which have
     * multiple builds power cannot use {@link GodStrategy} checkBuild, because it always return false if the Worker already built a block
     * (following the standard game rules).
     *
     * @param worker            the worker who want to build a new level.
     * @param buildCell         the cell in which the Player want to build a new level.
     * @param selectedWorker    to check if the worker that is trying to build is the same worker that performed movement.
     * @return true if the buildCount is less than the max number of times that the God can build each turn and other standard check are satisfied, false otherwise.
     */
    boolean checkBuild(Worker worker, Cell buildCell, Worker selectedWorker) {
        return worker.equals(selectedWorker) && worker.hasMoved() && canBuildAgain() && worker.getPosition().isAdjacentTo(buildCell) && (buildCell.getLevel() != BlockType.DOME) && (buildCell.isEmpty());
    }

}
