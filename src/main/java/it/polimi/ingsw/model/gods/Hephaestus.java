package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;

import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the Hephaestus strategy used by the Player who chose the powers of this God.
 * Specifically, Hephaestus allows the selected Worker to build on one additional block (not a dome)
 * on top of the first block built. For this purpose a {@link MultipleBuildDelegate} is used to perform 2 build phases,
 * while a {@link PreviousCellNeededDelegate} is used to check that the second build is done on the same previous
 * buildCell.
 *
 * @author Cosimo Sguanci
 */

public class Hephaestus extends GodStrategy {

    public static final String NAME = "Hephaestus";
    public static final String DESCRIPTION = "Description";
    public static final String POWER_DESCRIPTION = "Power Description";

    final int HEPHAESTUS_MAX_BUILD_NUM = 2;
    private MultipleBuildDelegate multipleBuildDelegate;

    /**
     * Used to save the previous cell where Hephaestus built a level.
     */
    private PreviousCellNeededDelegate previousCellNeededDelegate;

    @Override
    public Map<String, String> getGodInfo() {
        HashMap<String, String> info = new HashMap<>();
        info.put("name", NAME);
        info.put("description", DESCRIPTION);
        info.put("power_description", POWER_DESCRIPTION);
        return info;
    }

    public Hephaestus() {
        multipleBuildDelegate = new MultipleBuildDelegate(HEPHAESTUS_MAX_BUILD_NUM);
        previousCellNeededDelegate = new PreviousCellNeededDelegate();
    }

    /**
     * If it's the first building, this method calls the superclass (standard) checkBuild. Otherwise,
     * if the Player is trying to perform the second Build, the check is delegated to the MultipleBuildDelegate,
     * and additional controls are performed to ensure that the new buildCell is equal to the first Build Cell
     * and that the second level built it's not a Dome.
     *
     * @see GodStrategy#checkBuild(Worker, Cell, BlockType)
     * @see MultipleBuildDelegate#checkBuild(Worker, Cell, Worker)
     * @param worker    the worker who want to build a new level.
     * @param buildCell the cell in which the Player want to build a new level.
     * @return true if the Build passed as parameter can be performed, false otherwise.
     */
    @Override
    public boolean checkBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {

        if (multipleBuildDelegate.getBuildCount() == 0) {
            return super.checkBuild(worker, buildCell, buildCellBlockType);
        }

        Cell prevBuildCell = previousCellNeededDelegate.getPreviousCell();

        return multipleBuildDelegate.checkBuild(worker, buildCell, selectedWorker) && buildCell.equals(prevBuildCell) && buildCellBlockType != BlockType.DOME;
    }

    /**
     * Decorates the standard executeBuild increasing the build count of delegate and saving a reference to the last buildCell.
     *
     * @see GodStrategy#executeBuild(Worker, Cell, BlockType)
     * @param worker    the worker who want to build a new level.
     * @param buildCell the cell in which the Player want to build a new level.
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
     * @see GodStrategy#endTurn(Player)
     * @param player    The player whose turn is ending.
     */
    @Override
    public void endTurn(Player player) {
        super.endTurn(player);
        multipleBuildDelegate.reinitializeBuildCount();
        previousCellNeededDelegate.reinitializeCell();
    }
}
