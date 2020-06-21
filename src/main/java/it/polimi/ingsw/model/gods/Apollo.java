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
        if (selectedWorker != null)
            return false; // if selectedWorker is not null, move has already been executed this turn

        return !worker.hasMoved() && !worker.hasBuilt() && worker.getPosition().isAdjacentTo(moveCell)
                && worker.getPosition().isLevelDifferenceOk(moveCell)
                && (moveCell.isEmpty() || opponentWorkerMoverDelegate.isNotOtherPlayerWorkerPosition(worker, moveCell))
                && moveCell.getLevel() != BlockType.DOME;
    }

    /**
     * Implements standard execute worker movement, but if moveCell is not empty,
     * the opponent's worker that is occupying moveCell must be moved out of it,
     * and this is done delegating to {@link OpponentWorkerMoverDelegate}.
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

    /**
     * This method checks if Apollo's Player has at least an available Cell to move to.
     *
     * @param board the Match board to consider
     * @param player the Player whose ability to move is about to be tested
     * @return true if Apollo Player can move, false otherwise.
     * @see GodStrategy#canMove
     */
    @Override
    public boolean canMove(Board board, Player player) {
        Cell cellOne = player.getWorkerFirst().getPosition();
        Cell cellTwo = player.getWorkerSecond().getPosition();
        boolean possibleOne = canApolloMoveFromCell(board, cellOne);  //true if movement from cellOne is possible, so first worker can move somewhere
        boolean possibleTwo = canApolloMoveFromCell(board, cellTwo);  //true if movement from cellTwo is possible, so second worker can move somewhere
        return (possibleOne || possibleTwo);
    }

    /**
     * This method checks if Apollo's Worker can move from a specific Cell.
     * The main difference from a "standard" canMove, is that here we must consider that Apollo
     * can swap its position with an opponent Worker, so we avoid to check if adjacent cells are empty.
     *
     * @param board the Match board to consider
     * @param cell the Cell from which the worker would move
     * @return true if an Apollo movement is possible from the cell passed by parameter, false otherwise.
     */
    private boolean canApolloMoveFromCell(Board board, Cell cell) {

        for (int i = 0; i < Board.WIDTH_SIZE; i++) {
            for (int j = 0; j < Board.HEIGHT_SIZE; j++) {
                if (cell.isAdjacentTo(board.getCell(i, j)) && cell.isLevelDifferenceOk(board.getCell(i, j)) && board.getCell(i, j).getLevel() != BlockType.DOME) {
                    return true;
                }
            }
        }

        return false;
    }
}
