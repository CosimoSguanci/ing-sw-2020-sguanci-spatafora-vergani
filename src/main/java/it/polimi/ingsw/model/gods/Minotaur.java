package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.exceptions.InvalidCellException;
import it.polimi.ingsw.model.*;

/**
 * This class implements the Minotaur strategy used by the Player who chose the powers of this God.
 * Specifically, Minotaur allows the selected worker to move to an opponent Worker's space, if the
 * opponent Worker can be forced on space straight backwards to an unoccupied space at any level.
 *
 * @author Cosimo Sguanci
 */

public class Minotaur extends GodStrategy {

    public static final String NAME = "Minotaur";
    public static final String DESCRIPTION = "Bull-headed Monster";
    public static final String POWER_DESCRIPTION = "Your Move: Your Worker may move into an opponent Worker’s space, if their Worker can be forced one space straight backwards to an unoccupied space at any level";

    private final OpponentWorkerMoverDelegate opponentWorkerMoverDelegate;
    private Cell backwardCell;


    public Minotaur() {
        super(NAME, DESCRIPTION, POWER_DESCRIPTION);
        this.opponentWorkerMoverDelegate = new OpponentWorkerMoverDelegate();
    }

    /**
     * Implements standard controls on worker movement if the moveCell is empty. Otherwise,
     * it's necessary to check that the opponent worker's backward Cell is unoccupied (it needs
     * to be empty and without a Dome on it).
     *
     * @param worker   the worker that the Player wants to move.
     * @param moveCell the cell in which the Player want to move the worker.
     * @return true if the Move passed as parameter can be performed, false otherwise.
     * @see GodStrategy#checkMove(Worker, Cell)
     * @see Minotaur#computeBackwardCell(Board, Cell, Cell)
     */
    @Override
    public boolean checkMove(Worker worker, Cell moveCell) {

        if (moveCell.isEmpty())
            return super.checkMove(worker, moveCell);

        else {
            Worker otherWorker = worker.equals(worker.player.getWorkerFirst()) ? worker.player.getWorkerSecond() : worker.player.getWorkerFirst();
            try {
                backwardCell = computeBackwardCell(worker.board, worker.getPosition(), moveCell);

                return (selectedWorker == null || isUsingSelectedWorker(selectedWorker)) &&
                        !worker.hasMoved() &&
                        !worker.hasBuilt() &&
                        (moveCell.isEmpty() || !moveCell.getWorker().equals(otherWorker)) &&
                        worker.getPosition().isAdjacentTo(moveCell) &&
                        backwardCell.getRowIdentifier() < Board.WIDTH_SIZE &&
                        backwardCell.getColIdentifier() < Board.HEIGHT_SIZE &&
                        backwardCell.isEmpty() &&
                        backwardCell.getLevel() != BlockType.DOME;

            } catch (InvalidCellException e) {
                return false;
            }

        }

    }

    /**
     * Implements standard execute worker movement, but if moveCell is not empty,
     * the opponent's worker that is occupying moveCell must be moved to the backward Cell,
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
            opponentWorkerMoverDelegate.moveOpponentWorker(moveCell.getWorker(), backwardCell);
        }

        super.executeMove(worker, moveCell);
    }

    /**
     * This method finds opponent Worker backward Cell, using the original Player Worker to determine
     * the moving direction (diagonal or not).
     *
     * @param workerCell the position of worker that the Player wants to move.
     * @param moveCell   the cell in which the Player want to move the worker.
     * @return The Cell that is backward of moveCell.
     */
    private Cell computeBackwardCell(Board board, Cell workerCell, Cell moveCell) throws InvalidCellException {

        int backwardRow;
        int backwardCol;

        boolean isRowDiff = workerCell.getRowIdentifier() != moveCell.getRowIdentifier();
        boolean isColDiff = workerCell.getColIdentifier() != moveCell.getColIdentifier();

        if (isRowDiff && isColDiff) {
            // Diagonal

            if (workerCell.getRowIdentifier() > moveCell.getRowIdentifier()) {
                backwardRow = moveCell.getRowIdentifier() - 1;
            } else {
                backwardRow = moveCell.getRowIdentifier() + 1;
            }

            if (workerCell.getColIdentifier() > moveCell.getColIdentifier()) {
                backwardCol = moveCell.getColIdentifier() - 1;
            } else {
                backwardCol = moveCell.getColIdentifier() + 1;
            }

        } else {
            if (isRowDiff) {
                if (workerCell.getRowIdentifier() > moveCell.getRowIdentifier()) {
                    backwardRow = moveCell.getRowIdentifier() - 1;
                } else {
                    backwardRow = moveCell.getRowIdentifier() + 1;
                }

                backwardCol = moveCell.getColIdentifier();
            } else {
                if (workerCell.getColIdentifier() > moveCell.getColIdentifier()) {
                    backwardCol = moveCell.getColIdentifier() - 1;
                } else {
                    backwardCol = moveCell.getColIdentifier() + 1;
                }
                backwardRow = moveCell.getRowIdentifier();
            }
        }

        return board.getCell(backwardRow, backwardCol);
    }

    @Override
    public boolean canMove(Board board, Player player) {
        Cell cellOne = player.getWorkerFirst().getPosition();
        Cell cellTwo = player.getWorkerSecond().getPosition();
        boolean possibleOne = canMinotaurMoveFromCell(board, cellOne);
        boolean possibleTwo = canMinotaurMoveFromCell(board, cellTwo);
        return (possibleOne || possibleTwo);
    }

    private boolean canMinotaurMoveFromCell(Board board, Cell cell) {

        for (int i = 0; i < Board.WIDTH_SIZE; i++) {
            for (int j = 0; j < Board.HEIGHT_SIZE; j++) {
                if (cell.isAdjacentTo(board.getCell(i, j)) && cell.isLevelDifferenceOk(board.getCell(i, j)) && board.getCell(i, j).getLevel() != BlockType.DOME) {

                    if (board.getCell(i, j).isEmpty())
                        return true;
                    else {
                        try {
                            return computeBackwardCell(board, cell, board.getCell(i, j)).isEmpty();
                        } catch (InvalidCellException ignored) { // todo handle
                        }

                    }
                }
            }
        }

        return false;
    }
}
