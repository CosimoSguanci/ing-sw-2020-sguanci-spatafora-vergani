package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;

/**
 * This class implements the Poseidon strategy used by the Player who chose the powers of this God.
 * Specifically, at the end of the turn, if the Player's unmoved Worker is on the ground level, it can build
 * up to 3 times.
 *
 * @author Cosimo Sguanci
 */

public class Poseidon extends GodStrategy {
    final int POSEIDON_MAX_BUILD_NUM = 3;
    private MultipleBuildDelegate multipleBuildDelegate;
    private Worker unmovedWorker;

    public Poseidon() {
        multipleBuildDelegate = new MultipleBuildDelegate(POSEIDON_MAX_BUILD_NUM);
    }

    /**
     * If the Worker that is trying to build is the moved (selected) Worker, then superclass (standard) checkBuild is called.
     * Otherwise, custom checks are provided using {@link MultipleBuildDelegate} and standard Worker properties.
     * {@link MultipleBuildDelegate} checkBuild can't be used: the check about selectedWorker would fail because Poseidon allows
     * to build with the unselected Worker.
     *
     * @see GodStrategy#checkBuild(Worker, Cell, BlockType)
     * @param worker    the worker who want to build a new level.
     * @param buildCell the cell in which the Player want to build a new level.
     * @return true if the Build passed as parameter can be performed, false otherwise.
     */
    @Override
    public boolean checkBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        unmovedWorker = worker.player.getWorkerFirst().equals(selectedWorker) ?
                worker.player.getWorkerSecond() : worker.player.getWorkerFirst();

        if (!worker.equals(unmovedWorker)) {
            return super.checkBuild(worker, buildCell, buildCellBlockType);
        } else {
            if (worker.getPosition().getLevel() != BlockType.GROUND)
                return false;
            return multipleBuildDelegate.canBuildAgain() && worker.getPosition().isAdjacentTo(buildCell) && buildCell.getLevel() != BlockType.DOME && buildCell.isEmpty();
        }

    }

    /**
     * Decorates the standard executeBuild increasing the build count of {@link MultipleBuildDelegate},
     * if the Player is using unmoved Worker.
     *
     * @see GodStrategy#executeBuild(Worker, Cell, BlockType)
     * @param worker    the worker who want to build a new level.
     * @param buildCell the cell in which the Player want to build a new level.
     */
    @Override
    public void executeBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        super.executeBuild(worker, buildCell, buildCellBlockType);

        if(worker.equals(unmovedWorker))
            multipleBuildDelegate.increaseBuildCount();
    }

    /**
     * Calls superclass endTurn and resets delegates properties.
     *
     * @see GodStrategy#endTurn(Player)
     * @param player    The player whose turn is ending.
     */
    @Override
    public void endTurn(Player player) {
        super.endTurn(player);
        multipleBuildDelegate.reinitializeBuildCount();
    }
}
