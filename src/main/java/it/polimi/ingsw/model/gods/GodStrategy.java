package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;

import java.util.Map;

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
     // TODO consider checkGamePrepConstraints + endTurnConstraints?
    public abstract Map<String, String> getGodInfo(); // TODO FIX Duplicated GetGodInfo Impl

    /**
     * This property is useful for all Gods and it's used to keep track of the selected Worker
     * by a Player regarding the current turn. In fact, the standard flow imposes that the Player must use
     * the same Worker for both Moving and Building phases. However, with this implementation, this behaviours
     * is flexible and can be customized by Gods, if needed.
     */
    protected Worker selectedWorker;

    /**
     * Checks if the Worker passed is the selected Worker for the current turn.
     *
     * @param worker    The Worker to compare to the selected Worker
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
     */
    public boolean checkGamePreparation(Worker workerFirst, Cell workerFirstCell, Worker workerSecond, Cell workerSecondCell) {
        return workerFirstCell.isEmpty() && workerSecondCell.isEmpty();
    }

    /**
     * This method is called at every turn before executing the worker's movement.
     * It checks if the move which the player requested to do is allowed.
     * It can be extended by Gods which alter the standard movement rules.
     *
     * @see Worker#standardCheckMove(Cell)
     * @param worker   the worker that the Player wants to move.
     * @param moveCell the cell in which the Player want to move the worker.
     * @return true if the move passed as parameter can be performed, false otherwise.
     */
    public boolean checkMove(Worker worker, Cell moveCell) { // abstract ?
        return worker.standardCheckMove(moveCell);
    }

    /**
     * This method is called at every turn before executing the worker's build phase.
     * It checks if the build action which the player requested to do is allowed.
     * It can be extended by Gods which alter the standard build rules.
     *
     * @see Worker#standardCheckBuild(Cell)
     * @param worker    the worker who want to build a new level.
     * @param buildCell the cell in which the Player want to build a new level.
     * @return true if the Build passed as parameter can be performed, false otherwise.
     */
    public boolean checkBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        return isUsingSelectedWorker(worker) && worker.standardCheckBuild(buildCell);
    }

    /**
     * This method is called once a turn, to check if end turn constraints are satisfied.
     * The standard behavior is that a Player must move AND build with the selected worker in order to end the turn.
     * The gods that have a particular end turn behavior will override this method.
     */
    public boolean checkEndTurn() {
        return selectedWorker != null && selectedWorker.hasMoved() && selectedWorker.hasBuilt();
    }

    /**
     * This method is called once a match (for every player), to execute
     * the game preparation phase and respect the god's constraints.
     * The gods that have a particular Game preparation behavior will override this method.
     */
    public void executeGamePreparation(Worker workerFirst, Cell workerFirstCell, Worker workerSecond, Cell workerSecondCell) {
        workerFirst.setInitialPosition(workerFirstCell.getRowIdentifier(), workerFirstCell.getColIdentifier());
        workerSecond.setInitialPosition(workerSecondCell.getRowIdentifier(), workerSecondCell.getColIdentifier());
    }


    /**
     * This is the methods used to execute a movement action (change a worker's position).
     * It can be extended by Gods which alter the standard move action.
     *
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
     *
     * Setting the selectedWorker is necessary for Gods, like Prometheus, that can build BEFORE moving.
     * In that case the next move has to be performed with the same worker that built a level. [NO]
     *
     * @param worker    the worker who want to build a new level.
     * @param buildCell the cell in which the Player want to build a new level.
     */
    public void executeBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        worker.build(buildCell);
        //this.selectedWorker = worker;
    }

    /**
     * This is the method used to execute actions at the end of player's turn.
     * At least it's necessary to reset all flags of the Player Workers that are used
     * to check if a Worker has moved or built a block.
     *
     * @see Worker#reinitializeBuiltMoved()
     * @param player    Player corresponding to the current turn.
     */
    public void endTurn(Player player) {
        player.getWorkerFirst().reinitializeBuiltMoved();
        player.getWorkerSecond().reinitializeBuiltMoved();
    }

    /**
     * This is the method used to check if there are movement constraints imposed by other Players
     * that don't permit the Worker to execute a particular movement. So it's different from checkMove method
     * because that method does not consider possible actions prevented by other God powers.
     * However, the standard behavior is that there are not additional constraints besides standard and God-specific checkMove().
     *
     * @param oppositeWorker    Worker whose movement could be prevented by God's power
     * @param moveCell          The cell in which the Player want to move the worker.
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
     * @param oppositeWorker    Worker whose building action could be prevented by God's power
     * @param buildCell         the cell in which the Player want to build a new level.
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
     * @param oppositeWorker    Worker whose win could be prevented by God's power
     * @param moveCell          the cell in which the Player moved the selected Worker.
     * @return true if no constraint prevent the Worker to win, false otherwise.
     */
    public boolean checkWinConstraints(Worker oppositeWorker, Cell moveCell) { // Oppure interfacce per contraints e gamePrep? Con un isGamePrep, isConstraint...
        return true;
    }

    /**
     * This is the method used check if the Player has won in the current turn (it's called after executeMove and executeBuild).
     * It can be extended by the Gods that have additional Win Condition powers
     *
     * @param worker    Player's selected Worker corresponding to the current turn.
     * @return true if Worker's position level is 3 and it comes from level 2 (standard Win Condition triggered), false otherwise.
     */
    public boolean checkWinCondition(Worker worker) { // TODO hasMoved()?
        return worker.getPosition().getLevel() == BlockType.LEVEL_THREE && worker.getPreviousPositionBlockType() == BlockType.LEVEL_TWO; // TODO Win condition, if I go up two levels in a move?
    }


    public static GodStrategy instantiateGod(String god) {
        switch(god) {
            case "apollo" :
                return new Apollo();
            case "artemis" :
                return new Artemis();
            case "athena" :
                return new Athena();
            case "atlas" :
                return new Atlas();
            case "demeter" :
                return new Demeter();
            case "eros" :
                return new Eros();
            case "hephaestus" :
                return new Hephaestus();
            case "hera" :
                return new Hera();
            case "hestia" :
                return new Hestia();
            case "minotaur" :
                return new Minotaur();
            case "pan" :
                return new Pan();
            case "poseidon" :
                return new Poseidon();
            case "prometheus" :
                return new Prometheus();
            case "zeus" :
                return new Zeus();
            default:
                return new Zeus();  //TODO is this an exception? Client-side controls could not be all -> UnknownGodException
        }
    }


}