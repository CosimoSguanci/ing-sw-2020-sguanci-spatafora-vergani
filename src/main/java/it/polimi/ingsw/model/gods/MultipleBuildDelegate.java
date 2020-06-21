package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;

/**
 * This class represent the delegate that Gods can use if their power provides the possibility to build
 * more than one time each turn.
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

class MultipleBuildDelegate {
    private final int MAX_BUILD_COUNT;
    private int buildCount;

    MultipleBuildDelegate(int maxBuildCount) {
        this.MAX_BUILD_COUNT = maxBuildCount;
    }

    /**
     * buildCount getter
     *
     */
    int getBuildCount() {
        return buildCount;
    }

    /**
     * This method increases the God's build count in order to keep track of the numbers of levels built in a turn.
     *
     */
    void increaseBuildCount() {
        this.buildCount++;
    }

    /**
     * This method is resets the buildCount to zero.
     *
     */
    void reinitializeBuildCount() {
        this.buildCount = 0;
    }

    /**
     * This method is used to check if a God can build again in its turn.
     *
     * @return true if and only if the God can build at least another time
     */
    boolean canBuildAgain() {
        return buildCount < MAX_BUILD_COUNT;
    }

    /**
     * This method is used to check if the build action that Player attempted can be performed. It is necessary because the Gods which have
     * multiple builds power cannot use {@link GodStrategy} checkBuild, because it always return false if the Worker already built a block
     * (following the standard game rules).
     *
     * @param worker         the worker who want to build a new level.
     * @param buildCell      the cell in which the Player want to build a new level.
     * @param selectedWorker to check if the worker that is trying to build is the same worker that performed movement.
     * @return true if the buildCount is less than the max number of times that the God can build each turn and other standard check are satisfied, false otherwise.
     */
    boolean checkBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType, Worker selectedWorker) {
        return worker.equals(selectedWorker) &&
                worker.hasMoved() &&
                (buildCellBlockType == null || buildCellBlockType.getLevelNumber() == buildCell.getLevel().getLevelNumber() + 1) &&
                canBuildAgain() &&
                worker.getPosition().isAdjacentTo(buildCell) &&
                (buildCell.getLevel() != BlockType.DOME) &&
                (buildCell.isEmpty());
    }

}
