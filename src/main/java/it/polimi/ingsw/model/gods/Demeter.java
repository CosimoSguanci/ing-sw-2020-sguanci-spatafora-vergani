package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;

/**
 * This class implements the Demeter strategy used by the {@link Player} who chose the powers of this God.
 * Specifically, an Demeter {@link Worker} can build one additional time, but not in the same position.
 * {@link MultipleBuildDelegate} is used to handle multiple build phases, {@link PreviousCellNeededDelegate} is used to save
 * previous build cell in order to check constraints on the additional build.
 *
 * @author Cosimo Sguanci
 */

public class Demeter extends GodStrategy {

    public static final String NAME = "Demeter";
    public static final String DESCRIPTION = "Goddess of the Harvest";
    public static final String POWER_DESCRIPTION = "Your Build: Your Worker may build one additional time, but not on the same space";

    /**
     * Max number of levels buildable by Demeter in the same turn.
     */
    final int DEMETER_MAX_BUILD_NUM = 2;

    /**
     * Delegate used to handle the fact that Demeter can build more than one time in the same turn.
     */
    private final MultipleBuildDelegate multipleBuildDelegate;

    /**
     * Delegate used to handle the fact that Demeter cannot build in the same position twice in the same turn.
     */
    private final PreviousCellNeededDelegate previousCellNeededDelegate;


    public Demeter() {
        super(NAME, DESCRIPTION, POWER_DESCRIPTION);
        multipleBuildDelegate = new MultipleBuildDelegate(DEMETER_MAX_BUILD_NUM);
        previousCellNeededDelegate = new PreviousCellNeededDelegate();
    }

    /**
     * This method overrides checkBuild delegating to {@link MultipleBuildDelegate}
     * and checking that buildCell saved by {@link PreviousCellNeededDelegate} is different from the previous buildCell used.
     *
     * @param worker    the worker who want to build a new level.
     * @param buildCell the cell in which the Player want to build a new level.
     * @return true if the build passed as parameter can be performed, false otherwise.
     * @see MultipleBuildDelegate#checkBuild(Worker, Cell, BlockType, Worker)
     */
    @Override
    public boolean checkBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        return multipleBuildDelegate.checkBuild(worker, buildCell, buildCellBlockType, selectedWorker) &&
                (!previousCellNeededDelegate.hasPreviousCell() || !previousCellNeededDelegate.getPreviousCell().equals(buildCell));
    }

    /**
     * Decorates the standard executeBuild increasing the build count of {@link MultipleBuildDelegate}
     * and saving a reference to the last buildCell using {@link PreviousCellNeededDelegate}.
     *
     * @param worker    the worker who want to build a new level.
     * @param buildCell the cell in which the Player want to build a new level.
     * @see GodStrategy#executeBuild(Worker, Cell, BlockType)
     */
    @Override
    public void executeBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        super.executeBuild(worker, buildCell, buildCellBlockType);
        multipleBuildDelegate.increaseBuildCount();
        previousCellNeededDelegate.setPreviousCell(buildCell);
    }

    /**
     * This method calls superclass endTurn, then resets build count and previous cell at the end of Player's turn.
     *
     * @param player Player corresponding to the current turn.
     * @see GodStrategy#endPlayerTurn(Player)
     */
    @Override
    public void endPlayerTurn(Player player) {
        super.endPlayerTurn(player);
        multipleBuildDelegate.reinitializeBuildCount();
        previousCellNeededDelegate.reinitializeCell();
    }
}
