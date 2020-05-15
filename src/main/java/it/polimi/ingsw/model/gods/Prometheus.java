package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private final MultipleBuildDelegate multipleBuildDelegate;
    private boolean builtBeforeMoving;


    public Prometheus() {
        super(NAME, DESCRIPTION, POWER_DESCRIPTION);
        multipleBuildDelegate = new MultipleBuildDelegate(PROMETHEUS_MAX_BUILD_NUM);
    }

    /**
     * If the Player is trying to perform a Build BEFORE the Move phase, Prometheus power is activated.
     * Otherwise, {@link MultipleBuildDelegate} checkBuild is called.
     *
     * @param worker    the worker who want to build a new level.
     * @param buildCell the cell in which the Player want to build a new level.
     * @return true if the Build passed as parameter can be performed, false otherwise.
     * @see MultipleBuildDelegate#checkBuild(Worker, Cell, BlockType, Worker)
     */
    @Override
    public boolean checkBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {

        if (selectedWorker != null && !worker.equals(selectedWorker))
            return false;

        if (!worker.hasBuilt() && !worker.hasMoved()) { // First build (before moving)
            return worker.getPosition().isAdjacentTo(buildCell) &&
                    (buildCell.getLevel() != BlockType.DOME) &&
                    (buildCell.isEmpty()) &&
                    (buildCellBlockType == null || buildCellBlockType.getLevelNumber() == buildCell.getLevel().getLevelNumber() + 1);
        }

        return builtBeforeMoving
                ? multipleBuildDelegate.checkBuild(worker, buildCell, buildCellBlockType, selectedWorker)
                : super.checkBuild(worker, buildCell, buildCellBlockType);
    }

    /**
     * If the worker did not build before moving, Prometheus power is not activated and superclass (standard) checkMove is called.
     * Otherwise, the method checks that the Worker isn't going to a higher level. In this case we can't use standard {@link GodStrategy}
     * checkMove because it would check that the worker hasn't already build, so it would always fail.
     *
     * @param worker   the worker that the Player wants to move.
     * @param moveCell the cell in which the Player want to move the worker.
     * @return true if the Move passed as parameter can be performed, false otherwise.
     * @see GodStrategy#checkMove(Worker, Cell)
     */
    @Override
    public boolean checkMove(Worker worker, Cell moveCell) {
        if (!builtBeforeMoving)
            return super.checkMove(worker, moveCell);
        else
            return isUsingSelectedWorker(worker) && !worker.hasMoved() && worker.getPosition().isAdjacentTo(moveCell) && (moveCell.isEmpty()) && (moveCell.getLevel() != BlockType.DOME) && worker.getPosition().isLevelDifferenceOk(moveCell)
                    && moveCell.getLevel().getLevelNumber() <= worker.getPosition().getLevel().getLevelNumber();
    }

    @Override
    public void executeMove(Worker worker, Cell moveCell) {
        worker.move(moveCell);

        if (!builtBeforeMoving) { // not setting selectedWorker if builtBeforeMoving (it is set in executeBuild otherwise)
            this.selectedWorker = worker;
        }
    }

    /**
     * Decorates the standard executeBuild increasing the build count of {@link MultipleBuildDelegate}
     * and setting to true the flag that indicates if Prometheus power was activated, if the worker hasn't
     * already performed a move.
     *
     * @param worker    the worker who want to build a new level.
     * @param buildCell the cell in which the Player want to build a new level.
     * @see GodStrategy#executeBuild(Worker, Cell, BlockType)
     */
    @Override
    public void executeBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        super.executeBuild(worker, buildCell, buildCellBlockType);

        if (!worker.hasMoved()) {
            builtBeforeMoving = true;
            this.selectedWorker = worker;
        }

        multipleBuildDelegate.increaseBuildCount();

        if (builtBeforeMoving && multipleBuildDelegate.getBuildCount() == 1) {

            if (!worker.canMove()) worker.player.model.onPlayerLose(worker.player); // or: if availableCells.size() == 0

            else {
                List<Cell> availableCells = worker.board.getAvailableMoveCells(worker);

                List<Cell> feasibleMoveCells = new ArrayList<>();

                availableCells.forEach((moveCell) -> {
                    if (moveCell.getLevel().getLevelNumber() <= worker.getPosition().getLevel().getLevelNumber()) {
                        feasibleMoveCells.add(moveCell);
                    }
                });

                if (feasibleMoveCells.size() == 0) worker.player.model.onPlayerLose(worker.player);

                else {
                    List<Player> players = worker.player.model.getPlayers();

                    List<Cell> actualFeasibleMoveCells = feasibleMoveCells.stream().filter((moveCell) -> {
                        boolean actuallyFeasible = true;
                        for (Player p : players) {
                            if (!p.equals(worker.player)) {
                                if (!p.getGodStrategy().checkMoveConstraints(worker, moveCell)) {
                                    actuallyFeasible = false;
                                    break;
                                }
                            }
                        }
                        return actuallyFeasible;
                    }).collect(Collectors.toList());

                    if (actualFeasibleMoveCells.size() == 0) worker.player.model.onPlayerLose(worker.player);
                }

            }
        }
    }

    @Override
    public boolean checkEndTurn() {

        if (!builtBeforeMoving)
            return super.checkEndTurn();

        return super.checkEndTurn() && multipleBuildDelegate.getBuildCount() == PROMETHEUS_MAX_BUILD_NUM;
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
        builtBeforeMoving = false;
    }
}
