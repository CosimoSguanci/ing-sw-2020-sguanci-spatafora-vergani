package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.utils.GodsUtils;

import java.util.Objects;

/**
 * This is the abstract class all the gods must extend in order to be playable in the game.
 * This abstract class allows the use of Strategy Pattern, the God only knows it has a GodStrategy,
 * but it is at runtime that the right God powers are actually used.
 * The game turn can be divided in 2 main phases: Move and Build.
 * These phases must be checked before execution, and each god may implement specific
 * behaviors to both check and execute these operations.
 * However, standard check and execute actions are implemented in Worker class and called as standard actions,
 * allowing the Gods to also "decorate" the standard flow of the game.
 *
 * @author Cosimo Sguanci
 */

public abstract class GodStrategy {

    public final String NAME;
    public final String DESCRIPTION;
    public final String POWER_DESCRIPTION;
    /**
     * This property is useful for all Gods and it's used to keep track of the selected Worker
     * by a Player regarding the current turn. In fact, the standard flow imposes that the Player must use
     * the same Worker for both Moving and Building phases. However, with this implementation, this behaviours
     * is flexible and can be customized by Gods, if needed.
     */
    protected Worker selectedWorker;

    protected GodStrategy(String name, String description, String powerDescription) {
        NAME = name;
        DESCRIPTION = description;
        POWER_DESCRIPTION = powerDescription;
    }

    /**
     * This method uses {@link GodsUtils} factory method to return a GodStrategy instance.
     *
     * @param god String representation of the God to instantiate
     * @return The GodStrategy instance
     * @see GodsUtils#godsFactory(String)
     */
    public static GodStrategy instantiateGod(String god) {
        return GodsUtils.godsFactory(god);
    }

    /**
     * Checks if the Worker passed is the selected Worker for the current turn.
     *
     * @param worker The Worker to compare to the selected Worker
     * @return true if worker is the selected Worker, false otherwise.
     */
    protected boolean isUsingSelectedWorker(Worker worker) {
        return worker.equals(selectedWorker);
    }

    /**
     * This method is called once a match (for every player), to check
     * if game preparation constraints (if present) are satisfied.
     * The standard behavior is that no constraints are imposed to the game preparation phase.
     * The gods that have a particular Game preparation behavior will override this method.
     *
     * @param workerFirst      the first Worker placed
     * @param workerFirstCell  the position of the first Worker placed
     * @param workerSecond     the second Worker placed
     * @param workerSecondCell the position of the second Worker placed
     * @return true if the Workers can be placed in the positions specified, false otherwise.
     */
    public boolean checkGamePreparation(Worker workerFirst, Cell workerFirstCell, Worker workerSecond, Cell workerSecondCell) {
        return workerFirstCell.isEmpty() && workerSecondCell.isEmpty();
    }

    /**
     * This method is called once a match (for every player), to check
     * if game preparation constraints from other Player's Gods (if present) are satisfied.
     * The standard behavior is that no constraints are imposed to the game preparation phase.
     * The gods that impose particular Game preparation constraints will override this method.
     * However, in this version, there are not Gods which set Game Preparation constraints.
     *
     * @param workerFirst      the first Worker placed
     * @param workerFirstCell  the position of the first Worker placed
     * @param workerSecond     the second Worker placed
     * @param workerSecondCell the position of the second Worker placed
     * @return true if the Workers can be placed in the positions specified, false otherwise.
     */
    public boolean checkGamePreparationConstraints(Worker workerFirst, Cell workerFirstCell, Worker workerSecond, Cell workerSecondCell) {
        return true;
    }

    /**
     * This method is called at every turn before executing the worker's movement.
     * It checks if the move which the player requested to do is allowed.
     * It can be extended by Gods which alter the standard movement rules.
     *
     * @param worker   the worker that the Player wants to move.
     * @param moveCell the cell in which the Player want to move the worker.
     * @return true if the move passed as parameter can be performed, false otherwise.
     * @see Worker#standardCheckMove(Cell)
     */
    public boolean checkMove(Worker worker, Cell moveCell) { // abstract ?
        return worker.standardCheckMove(moveCell) && (selectedWorker == null || isUsingSelectedWorker(worker));
    }

    /**
     * This method is called at every turn before executing the worker's build phase.
     * It checks if the build action which the player requested to do is allowed.
     * It can be extended by Gods which alter the standard build rules.
     *
     * @param worker             the worker who want to build a new level.
     * @param buildCell          the cell in which the Player want to build a new level.
     * @param buildCellBlockType [optional] the level that the Worker wants to build.
     * @return true if the Build passed as parameter can be performed, false otherwise.
     * @see Worker#standardCheckBuild(Cell, BlockType)
     */
    public boolean checkBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        return isUsingSelectedWorker(worker) && (buildCellBlockType == null || buildCellBlockType.getLevelNumber() == buildCell.getLevel().getLevelNumber() + 1) && worker.standardCheckBuild(buildCell, buildCellBlockType);
    }

    /**
     * This method is called once a turn, to check if end turn constraints are satisfied.
     * The standard behavior is that a Player must move AND build with the selected worker in order to end the turn.
     * The gods that have a particular end turn behavior will override this method.
     *
     * @return true if the turn can be ended now, false otherwise.
     */
    public boolean checkEndTurn() {
        return selectedWorker != null && selectedWorker.hasMoved() && selectedWorker.hasBuilt();
    }

    /**
     * This method is called once a turn, to check if end turn constraints from other Player's Gods (if present) are satisfied.
     * The standard behavior is that there are no constraints other than those imposed by checkEndTurn.
     * The gods that impose particular End Turn constraints will override this method.
     * However, in this version, there are not Gods which set End Turn constraints.
     *
     * @param player the Player that wants to end its turn.
     * @return true if the turn can be ended now, false otherwise.
     */
    public boolean checkEndTurnConstraints(Player player) {
        return true;
    }

    /**
     * This method is called once a match (for every player), to execute
     * the game preparation phase and respect the god's constraints.
     * The gods that have a particular Game preparation behavior will override this method.
     *
     * @param workerFirst      the first Worker placed
     * @param workerFirstCell  the position of the first Worker placed
     * @param workerSecond     the second Worker placed
     * @param workerSecondCell the position of the second Worker placed
     */
    public void executeGamePreparation(Worker workerFirst, Cell workerFirstCell, Worker workerSecond, Cell workerSecondCell) {
        workerFirst.setInitialPosition(workerFirstCell.getRowIdentifier(), workerFirstCell.getColIdentifier());
        workerSecond.setInitialPosition(workerSecondCell.getRowIdentifier(), workerSecondCell.getColIdentifier());
    }

    /**
     * This is the methods used to execute a movement action (change a worker's position).
     * It can be extended by Gods which alter the standard move action.
     * <p>
     * It also sets the current selected Worker for the Player (normal flow implies that a Worker first moves, then builds a block)
     *
     * @param worker   the worker that the Player wants to move.
     * @param moveCell the cell in which the Player want to move the worker.
     */
    public void executeMove(Worker worker, Cell moveCell) {
        worker.move(moveCell);
        this.selectedWorker = worker;
    }

    /**
     * This is the method used to execute a build action (change a cell level).
     * It can be extended by Gods which alter the standard build action.
     * <p>
     * Setting the selectedWorker is necessary for Gods, like Prometheus, that can build BEFORE moving.
     * In that case the next move has to be performed with the same worker that built a level. [NO]
     *
     * @param worker             the worker who want to build a new level.
     * @param buildCell          the cell in which the Player want to build a new level.
     * @param buildCellBlockType [optional] the level that the Worker wants to build.
     */
    public void executeBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        worker.build(buildCell, buildCellBlockType);
    }

    /**
     * This is the method used to execute actions at the end of player's turn.
     * At least it's necessary to reset all flags of the Player Workers that are used
     * to check if a Worker has moved or built a block.
     *
     * @param player Player corresponding to the current turn.
     * @see Worker#reinitializeBuiltMoved()
     */
    public void endPlayerTurn(Player player) {
        this.selectedWorker = null;
        player.getWorkerFirst().reinitializeBuiltMoved();
        player.getWorkerSecond().reinitializeBuiltMoved();
    }

    /**
     * This method is a callback which is called every time the player's turn starts.
     *
     * @param player The Player whose turn just started
     */
    public void onTurnStarted(Player player) {
    }

    /**
     * This is the method used to check if there are movement constraints imposed by other Players
     * that don't permit the Worker to execute a particular movement. So it's different from checkMove method
     * because that method does not consider possible actions prevented by other God powers.
     * However, the standard behavior is that there are not additional constraints besides standard and God-specific checkMove().
     *
     * @param oppositeWorker Worker whose movement could be prevented by God's power
     * @param moveCell       The cell in which the Player want to move the worker.
     * @return true if no constraint prevent the move passed as parameter, false otherwise.
     */
    public boolean checkMoveConstraints(Worker oppositeWorker, Cell moveCell) {
        return true;
    }

    /**
     * This is the method used to check if there are build constraints imposed by other Players
     * that don't permit the Worker to execute a particular building action. So it's different from checkBuild method
     * because that method does not consider possible actions prevented by other God powers.
     * However, the standard behavior is that there are not additional constraints besides standard and God-specific checkBuild().
     *
     * @param oppositeWorker     Worker whose building action could be prevented by God's power
     * @param buildCell          the cell in which the Player want to build a new level.
     * @param buildCellBlockType [optional] the level that the Worker wants to build.
     * @return true if no constraint prevent the move passed as parameter, false otherwise.
     */
    public boolean checkBuildConstraints(Worker oppositeWorker, Cell buildCell, BlockType buildCellBlockType) {
        return true;
    }

    /**
     * This is the method used to check if there are win constraints imposed by other Players
     * that don't permit the Worker to win under certain conditions. So it's different from checkWinCondition method
     * because that method does not consider possible win conditions prevented by other God powers.
     * However, the standard behavior is that there are not additional constraints besides standard and God-specific checkBuild().
     *
     * @param oppositeWorker Worker whose win could be prevented by God's power
     * @param moveCell       the cell in which the Player moved the selected Worker.
     * @return true if no constraint prevent the Worker to win, false otherwise.
     */
    public boolean checkWinConstraints(Worker oppositeWorker, Cell moveCell) { // Oppure interfacce per contraints e gamePrep? Con un isGamePrep, isConstraint...
        return true;
    }

    /**
     * This is the method used check if the Player has won in the current turn (it's called after executeMove and executeBuild).
     * It can be extended by the Gods that have additional Win Condition powers
     *
     * @param worker Player's selected Worker corresponding to the current turn.
     * @return true if Worker's position level is 3 and it comes from level 2 (standard Win Condition triggered), false otherwise.
     */
    public boolean checkWinCondition(Worker worker) {
        return worker.hasMoved() && !worker.hasBuilt() && worker.getPosition().getLevel() == BlockType.LEVEL_THREE && worker.getPreviousPositionBlockType() == BlockType.LEVEL_TWO;
    }

    /**
     * This method is called to check if a player can move to any adjacent Cell. It's useful to check if a Player lost when its turn starts.
     *
     * @param board  the Match board to consider
     * @param player the player whose ability to move is about to be tested
     * @return true if the Player passed by parameter has an adjacent available Cell to move to, false otherwise.
     * @see GodStrategy#canBuild(Board, Worker)
     */
    public boolean canMove(Board board, Player player) {
        return board.canMove(player);
    }

    /**
     * This method is called to check if a worker can build to any adjacent Cell. It's useful to check if a Player lost after a move.
     *
     * @param board  the Match board to consider
     * @param worker the worker whose ability to build is about to be tested
     * @return true if the Worker passed by parameter has an adjacent buildable Cell, false otherwise.
     * @see GodStrategy#canBuild(Board, Worker)
     */
    public boolean canBuild(Board board, Worker worker) {
        return board.canBuild(worker);
    }

    /**
     * Classic "equals" overridden version, used to check if two GodStrategy instances are equals.
     *
     * @param o the object to be compared to this
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GodStrategy that = (GodStrategy) o;
        return Objects.equals(NAME, that.NAME) &&
                Objects.equals(DESCRIPTION, that.DESCRIPTION) &&
                Objects.equals(POWER_DESCRIPTION, that.POWER_DESCRIPTION);
    }

}