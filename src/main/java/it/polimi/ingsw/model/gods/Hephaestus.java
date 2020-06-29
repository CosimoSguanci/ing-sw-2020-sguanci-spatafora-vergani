package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;

/**
 * This class implements the Hephaestus strategy used by the {@link Player} who chose the powers of this God.
 * Specifically, Hephaestus allows the selected {@link Worker} to build on one additional block (not a dome)
 * on top of the first block built. For this purpose a {@link MultipleBuildDelegate} is used to perform 2 build phases,
 * while a {@link PreviousCellNeededDelegate} is used to check that the second build is done on the same previous
 * buildCell.
 *
 * @author Cosimo Sguanci
 */

public class Hephaestus extends GodStrategy {

    public static final String NAME = "Hephaestus";
    public static final String DESCRIPTION = "God of Blacksmiths";
    public static final String POWER_DESCRIPTION = "Your Build: Your Worker may build one additional block (not dome) on top of your first block";

    /**
     * Max number of levels buildable by Hephaestus in the same turn.
     */
    final int HEPHAESTUS_MAX_BUILD_NUM = 2;

    /**
     * Delegate used to handle the fact that Hephaestus can build more than one time in the same turn.
     */
    private final MultipleBuildDelegate multipleBuildDelegate;

    /**
     * Used to save the previous cell where Hephaestus built a level.
     */
    private final PreviousCellNeededDelegate previousCellNeededDelegate;


    public Hephaestus() {
        super(NAME, DESCRIPTION, POWER_DESCRIPTION);
        multipleBuildDelegate = new MultipleBuildDelegate(HEPHAESTUS_MAX_BUILD_NUM);
        previousCellNeededDelegate = new PreviousCellNeededDelegate();
    }

    /**
     * If it's the first building, this method calls the superclass (standard) checkBuild. Otherwise,
     * if the player is trying to perform the second build action, the check is delegated to the {@link MultipleBuildDelegate},
     * and additional controls are performed to ensure that the new buildCell is equal to the first build Cell
     * and that the second level built it's not a Dome.
     *
     * @param worker    the worker who want to build a new level.
     * @param buildCell the cell in which the Player want to build a new level.
     * @return true if the Build passed as parameter can be performed, false otherwise.
     * @see GodStrategy#checkBuild(Worker, Cell, BlockType)
     * @see MultipleBuildDelegate#checkBuild(Worker, Cell, BlockType, Worker)
     */
    @Override
    public boolean checkBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {

        if (multipleBuildDelegate.getBuildCount() == 0) {
            return super.checkBuild(worker, buildCell, buildCellBlockType);
        }

        Cell prevBuildCell = previousCellNeededDelegate.getPreviousCell();

        return multipleBuildDelegate.checkBuild(worker, buildCell, buildCellBlockType, selectedWorker) && buildCell.equals(prevBuildCell) && buildCellBlockType != BlockType.DOME && prevBuildCell.getLevel() != BlockType.LEVEL_THREE;
    }

    /**
     * Decorates the standard executeBuild increasing the build count of delegate and saving a reference to the last buildCell.
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
     * Calls superclass endTurn and resets delegates properties.
     *
     * @param player The player whose turn is ending.
     * @see GodStrategy#endPlayerTurn(Player)
     */
    @Override
    public void endPlayerTurn(Player player) {
        super.endPlayerTurn(player);
        multipleBuildDelegate.reinitializeBuildCount();
        previousCellNeededDelegate.reinitializeCell();
    }
}
