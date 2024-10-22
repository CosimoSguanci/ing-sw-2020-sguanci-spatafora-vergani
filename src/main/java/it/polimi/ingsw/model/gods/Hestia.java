package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;

/**
 * This class implements the Hestia strategy used by the {@link Player} who chose the powers of this God.
 * Specifically, Hestia allows the selected worker to build an additional time using {@link MultipleBuildDelegate}, but not on a perimeter space.
 *
 * @author Cosimo Sguanci
 */

public class Hestia extends GodStrategy {

    public static final String NAME = "Hestia";
    public static final String DESCRIPTION = "Goddess of Hearth and Home";
    public static final String POWER_DESCRIPTION = "Your Build: Your Worker may build one additional time, but this cannot be on a perimeter space";

    /**
     * Max number of levels buildable by Hestia in the same turn.
     */
    final int HESTIA_MAX_BUILD_NUM = 2;

    /**
     * Delegate used to handle the fact that Hestia can build more than one time in the same turn.
     */
    private final MultipleBuildDelegate multipleBuildDelegate;

    public Hestia() {
        super(NAME, DESCRIPTION, POWER_DESCRIPTION);
        multipleBuildDelegate = new MultipleBuildDelegate(HESTIA_MAX_BUILD_NUM);
    }

    /**
     * This method calls {@link MultipleBuildDelegate} checkBuild; if the return value is false, it means that the Build cannot be performed.
     * Otherwise, if it's the first build, the method returns true, while if it's the second Build action, an additional control is
     * done to allow second builds only on cells that are not on the Board perimeter.
     *
     * @param worker    the worker who want to build a new level.
     * @param buildCell the cell in which the Player want to build a new level.
     * @return true if the Build passed as parameter can be performed, false otherwise.
     * @see MultipleBuildDelegate#checkBuild(Worker, Cell, BlockType, Worker)
     * @see Hestia#isPerimeterCell(Cell)
     */
    @Override
    public boolean checkBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        if (!multipleBuildDelegate.checkBuild(worker, buildCell, buildCellBlockType, selectedWorker))
            return false;
        return multipleBuildDelegate.getBuildCount() != HESTIA_MAX_BUILD_NUM - 1 || !isPerimeterCell(buildCell);
    }

    /**
     * Decorates the standard executeBuild increasing the build count of {@link MultipleBuildDelegate}
     *
     * @param worker    the worker who want to build a new level.
     * @param buildCell the cell in which the Player want to build a new level.
     * @see GodStrategy#executeBuild(Worker, Cell, BlockType)
     */
    @Override
    public void executeBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        super.executeBuild(worker, buildCell, buildCellBlockType);
        multipleBuildDelegate.increaseBuildCount();
    }

    /**
     * Checks if the {@link Cell} passed is on the perimeter of the game {@link Board}.
     *
     * @param cell the Cell in which the Opponent Worker was moved
     * @return true if the Cell is in the perimeter of the game Board, false otherwise.
     */
    private boolean isPerimeterCell(Cell cell) {
        return cell.getColIdentifier() == Board.WIDTH_SIZE - 1 || cell.getRowIdentifier() == Board.HEIGHT_SIZE - 1
                || cell.getColIdentifier() == 0 || cell.getRowIdentifier() == 0;
    }

    /**
     * This method calls superclass endTurn, then resets build count at the end of Player's turn.
     *
     * @param player Player corresponding to the current turn.
     * @see GodStrategy#endPlayerTurn(Player)
     */
    @Override
    public void endPlayerTurn(Player player) {
        super.endPlayerTurn(player);
        multipleBuildDelegate.reinitializeBuildCount();
    }
}
