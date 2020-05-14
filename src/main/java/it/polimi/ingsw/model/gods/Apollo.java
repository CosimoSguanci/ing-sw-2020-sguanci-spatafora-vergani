package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;


/**
 * This class implements the Apollo strategy used by the Player who chose the powers of this God.
 * Specifically, Apollo allows the Worker to move to a Cell which is occupied by another player's worker,
 * swapping the worker's positions.
 *
 * @author Cosimo Sguanci
 */

public class Apollo extends GodStrategy {

    public static final String NAME = "Apollo";
    public static final String DESCRIPTION = "God Of Music";
    public static final String POWER_DESCRIPTION = "Your Move: Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated";

    /**
     * Delegation pattern (composition over inheritance) is used to share common behaviours
     * between gods and to allow a single god to inherit multiple common properties.
     * <p>
     * {@link OpponentWorkerMoverDelegate} is used by Apollo to swap positions with an opponent worker, if necessary.
     */
    private final OpponentWorkerMoverDelegate opponentWorkerMoverDelegate;

    public Apollo() {
        super(NAME, DESCRIPTION, POWER_DESCRIPTION);
        this.opponentWorkerMoverDelegate = new OpponentWorkerMoverDelegate();
    }

    /**
     * Implements standard controls on worker movement, except the check about emptiness of the new position
     * (Apollo player's worker can be swapped with an adjacent worker).
     *
     * @param worker   the worker that the Player wants to move.
     * @param moveCell the cell in which the Player want to move the worker.
     * @return true if the Move passed as parameter can be performed, false otherwise.
     */
    @Override
    public boolean checkMove(Worker worker, Cell moveCell) {
        return (selectedWorker == null || isUsingSelectedWorker(worker))
                && !worker.hasMoved() && !worker.hasBuilt() && worker.getPosition().isAdjacentTo(moveCell)
                && worker.getPosition().isLevelDifferenceOk(moveCell)
                && moveCell.getLevel() != BlockType.DOME;
    }

    /**
     * Implements standard execute worker movement, but if moveCell is not empty,
     * the opponent's worker that is occupying moveCell must be moved out of it,
     * and this is done delegating to {@link OpponentWorkerMoverDelegate}..
     *
     * @param worker   the worker that the Player wants to move.
     * @param moveCell the cell in which the Player want to move the worker.
     * @see GodStrategy#executeMove(Worker, Cell)
     * @see OpponentWorkerMoverDelegate#moveOpponentWorker(Worker, Cell)
     */
    @Override
    public void executeMove(Worker worker, Cell moveCell) {

        if (!moveCell.isEmpty()) {
            opponentWorkerMoverDelegate.moveOpponentWorker(moveCell.getWorker(), worker.getPosition());
        }

        super.executeMove(worker, moveCell);
    }

    @Override
    public boolean canMove(Board board, Player player) {
        Cell cellOne = player.getWorkerFirst().getPosition();  //actual cell of the first worker
        Cell cellTwo = player.getWorkerSecond().getPosition();  //actual cell of the second worker
        boolean possibleOne = canApolloMoveFromCell(board, cellOne);  //true if movement from cellOne is possible, so first worker can move somewhere
        boolean possibleTwo = canApolloMoveFromCell(board, cellTwo);  //true if movement from cellTwo is possible, so second worker can move somewhere
        return (possibleOne || possibleTwo);
    }

    private boolean canApolloMoveFromCell(Board board, Cell cell) {

        for (int i = 0; i < Board.WIDTH_SIZE; i++) {
            for (int j = 0; j < Board.HEIGHT_SIZE; j++) {
                if (cell.isLevelDifferenceOk(board.getCell(i, j)) && board.getCell(i, j).getLevel() != BlockType.DOME) {
                    return true;
                }
            }
        }

        return false;
    }
}
