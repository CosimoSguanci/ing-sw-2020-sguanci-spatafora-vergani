package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;

import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the Prometheus strategy used by the Player who chose the powers of this God.
 * Specifically, if the selected Worker does not move up, it can build both before and after moving. 
 *
 * @author Cosimo Sguanci
 */

public class Prometheus extends GodStrategy {

    public static final String NAME = "Prometheus";
    public static final String DESCRIPTION = "Titan Benefactor of Mankind";
    public static final String POWER_DESCRIPTION = "Your Turn: If your Worker does not move up, it may build both before and after moving.";

    final int PROMETHEUS_MAX_BUILD_NUM = 2;
    private MultipleBuildDelegate multipleBuildDelegate;
    private boolean builtBeforeMoving;

    @Override
    public Map<String, String> getGodInfo() {
        HashMap<String, String> info = new HashMap<>();
        info.put("name", NAME);
        info.put("description", DESCRIPTION);
        info.put("power_description", POWER_DESCRIPTION);
        return info;
    }

    public Prometheus() {
        multipleBuildDelegate = new MultipleBuildDelegate(PROMETHEUS_MAX_BUILD_NUM);
    }

    /**
     * If the Player is trying to perform a Build BEFORE the Move phase, Prometheus power is activated.
     * Otherwise, {@link MultipleBuildDelegate} checkBuild is called.
     *
     * @see MultipleBuildDelegate#checkBuild(Worker, Cell, Worker)
     * @param worker    the worker who want to build a new level.
     * @param buildCell the cell in which the Player want to build a new level.
     * @return true if the Build passed as parameter can be performed, false otherwise.
     */
    @Override
    public boolean checkBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {

        if (!worker.hasBuilt() && !worker.hasMoved()) { // First build (before moving)
            return worker.getPosition().isAdjacentTo(buildCell) && (buildCell.getLevel() != BlockType.DOME) && (buildCell.isEmpty());
        }


        return builtBeforeMoving
                ? multipleBuildDelegate.checkBuild(worker, buildCell, selectedWorker)
                : super.checkBuild(worker, buildCell, buildCellBlockType);
    }

    /**
     * If the worker did not build before moving, Prometheus power is not activated and superclass (standard) checkMove is called.
     * Otherwise, the method checks that the Worker isn't going to a higher level. In this case we can't use standard {@link GodStrategy}
     * checkMove because it would check that the worker hasn't already build, so it would always fail.
     *
     * @see GodStrategy#checkMove(Worker, Cell) 
     * @param worker   the worker that the Player wants to move.
     * @param moveCell the cell in which the Player want to move the worker.
     * @return true if the Move passed as parameter can be performed, false otherwise.
     */
    @Override
    public boolean checkMove(Worker worker, Cell moveCell) {
        if (!builtBeforeMoving)
            return super.checkMove(worker, moveCell);
        else
            return isUsingSelectedWorker(worker) && !worker.hasMoved() && worker.getPosition().isAdjacentTo(moveCell) && (moveCell.isEmpty()) && (moveCell.getLevel() != BlockType.DOME) && worker.getPosition().isLevelDifferenceOk(moveCell)
                    && moveCell.getLevel().getLevelNumber() <= worker.getPosition().getLevel().getLevelNumber();
    }

    /**
     * Decorates the standard executeBuild increasing the build count of {@link MultipleBuildDelegate}
     * and setting to true the flag that indicates if Prometheus power was activated, if the worker hasn't
     * already performed a move.
     *
     * @see GodStrategy#executeBuild(Worker, Cell, BlockType) 
     * @param worker    the worker who want to build a new level.
     * @param buildCell the cell in which the Player want to build a new level.
     */
    @Override
    public void executeBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        super.executeBuild(worker, buildCell, buildCellBlockType);

        if (!worker.hasMoved())
            builtBeforeMoving = true;

        multipleBuildDelegate.increaseBuildCount();
        this.selectedWorker = worker;
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
