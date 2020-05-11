package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;

/**
 * This class implements the Poseidon strategy used by the Player who chose the powers of this God.
 * Specifically, at the end of the turn, if the Player's unmoved Worker is on the ground level, it can build
 * up to 3 times.
 *
 * @author Cosimo Sguanci
 */

public class Poseidon extends GodStrategy {

    public static final String NAME = "Poseidon";
    public static final String DESCRIPTION = "God of the Sea";
    public static final String POWER_DESCRIPTION = "End of Your Turn: If your unmoved Worker is on the ground level, it may build up to three times.";

    final int POSEIDON_MAX_BUILD_NUM = 3;
    private MultipleBuildDelegate multipleBuildDelegate;
    private Worker unmovedWorker;


    public Poseidon() {
        super(NAME, DESCRIPTION, POWER_DESCRIPTION);
        multipleBuildDelegate = new MultipleBuildDelegate(POSEIDON_MAX_BUILD_NUM);
    }

    /**
     * If the Worker that is trying to build is the moved (selected) Worker, then superclass (standard) checkBuild is called.
     * Otherwise, custom checks are provided using {@link MultipleBuildDelegate} and standard Worker properties.
     * {@link MultipleBuildDelegate} checkBuild can't be used: the check about selectedWorker would fail because Poseidon allows
     * to build with the unselected Worker.
     *
     * @param worker    the worker who want to build a new level.
     * @param buildCell the cell in which the Player want to build a new level.
     * @return true if the Build passed as parameter can be performed, false otherwise.
     * @see GodStrategy#checkBuild(Worker, Cell, BlockType)
     */
    @Override
    public boolean checkBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        unmovedWorker = worker.player.getWorkerFirst().equals(selectedWorker) ?
                worker.player.getWorkerSecond() : worker.player.getWorkerFirst();

        if (!worker.equals(unmovedWorker)) { // TODO check, turn must be ended?
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
     * @param worker    the worker who want to build a new level.
     * @param buildCell the cell in which the Player want to build a new level.
     * @see GodStrategy#executeBuild(Worker, Cell, BlockType)
     */
    @Override
    public void executeBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        super.executeBuild(worker, buildCell, buildCellBlockType);

        if (worker.equals(unmovedWorker))
            multipleBuildDelegate.increaseBuildCount();
    }

    /**
     * Calls superclass endTurn and resets delegates properties.
     *
     * @param player The player whose turn is ending.
     * @see GodStrategy#endPlayerTurn(Player)
     */
    @Override
    public void endPlayerTurn(Player player) {
        super.endPlayerTurn(player);
        multipleBuildDelegate.reinitializeBuildCount();
    }
}
