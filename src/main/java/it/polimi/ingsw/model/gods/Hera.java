package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;

/**
 * This class implements the Hera strategy used by the Player who chose the powers of this God.
 * Specifically, Hera imposes Win constraints to other players: an opponent cannot win by moving into a perimeter space.
 *
 * @author Cosimo Sguanci
 */

public class Hera extends GodStrategy {

    public static final String NAME = "Hera";
    public static final String DESCRIPTION = "Godness of Marriage";
    public static final String POWER_DESCRIPTION = "Opponent’s Turn: An opponent cannot win by moving into a perimeter space.";

    public Hera() {
        super(NAME, DESCRIPTION, POWER_DESCRIPTION);
    }


    /**
     * Implements Hera Win Constraints to other players.
     *
     * @param opponentWorker the opponent selected worker for the current turn.
     * @param moveCell       the cell in which the opponent Player moved the selected worker.
     * @return true if the Cell where the opposite Worker moved is not a perimeter Cell, false otherwise
     * @see Hera#isPerimeterCell(Cell)
     */
    @Override
    public boolean checkWinConstraints(Worker opponentWorker, Cell moveCell) {
        return !isPerimeterCell(moveCell);
    }

    /**
     * Checks if the Cell passed is on the perimeter of the game Board.
     *
     * @param cell the Cell in which the Opponent Worker was moved
     * @return true if the Cell is in the perimeter of the game Board, false otherwise.
     */
    private boolean isPerimeterCell(Cell cell) {
        return cell.getColIdentifier() == Board.WIDTH_SIZE - 1 || cell.getRowIdentifier() == Board.HEIGHT_SIZE - 1
                || cell.getColIdentifier() == 0 || cell.getRowIdentifier() == 0;
    }
}
