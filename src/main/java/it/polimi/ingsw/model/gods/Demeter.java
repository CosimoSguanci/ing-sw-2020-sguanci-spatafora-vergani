package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;

import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the Demeter strategy used by the Player who chose the powers of this God.
 * Specifically, an Demeter Worker can build one additional time, but not at the same time.
 * {@link MultipleBuildDelegate} is used to handle multiple build phases, {@link PreviousCellNeededDelegate} is used to save
 * previous build cell in order to check constraints on the additional build.
 *
 * @author Cosimo Sguanci
 */

public class Demeter extends GodStrategy {

    public static final String NAME = "Demeter";
    public static final String DESCRIPTION = "Description";
    public static final String POWER_DESCRIPTION = "Power Description";

    final int DEMETER_MAX_BUILD_NUM = 2;
    private MultipleBuildDelegate multipleBuildDelegate;
    private PreviousCellNeededDelegate previousCellNeededDelegate;

    @Override
    public Map<String, String> getGodInfo() {
        HashMap<String, String> info = new HashMap<>();
        info.put("name", NAME);
        info.put("description", DESCRIPTION);
        info.put("power_description", POWER_DESCRIPTION);
        return info;
    }

    public Demeter() {
        multipleBuildDelegate = new MultipleBuildDelegate(DEMETER_MAX_BUILD_NUM);
        previousCellNeededDelegate = new PreviousCellNeededDelegate();
    }

    /**
     * This method overrides checkBuild delegating to {@link MultipleBuildDelegate}
     * and checking that buildCell saved by {@link PreviousCellNeededDelegate} is different from the previous buildCell used.
     *
     * @see MultipleBuildDelegate#checkBuild(Worker, Cell, Worker)
     * @param worker    the worker who want to build a new level.
     * @param buildCell the cell in which the Player want to build a new level.
     * @return true if the build passed as parameter can be performed, false otherwise.
     */
    @Override
    public boolean checkBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        return multipleBuildDelegate.checkBuild(worker, buildCell, selectedWorker) &&
                (!previousCellNeededDelegate.hasPreviousCell() ||
                !(previousCellNeededDelegate.getPreviousCell().equals(buildCell)));
    }

    /**
     * Decorates the standard executeBuild increasing the build count of {@link MultipleBuildDelegate}
     * and saving a reference to the last buildCell using {@link PreviousCellNeededDelegate}.
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
     * This method calls superclass endTurn, then resets build count and previous cell at the end of Player's turn.
     *
     * @see GodStrategy#endTurn(Player)
     * @param player    Player corresponding to the current turn.
     */
    @Override
    public void endTurn(Player player) {
        super.endTurn(player);
        multipleBuildDelegate.reinitializeBuildCount();
        previousCellNeededDelegate.reinitializeCell();
    }
}
