package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;

import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the Hera strategy used by the Player who chose the powers of this God.
 * Specifically, Hera imposes Win constraints to other players: an opponent cannot win by moving into a perimeter space.
 *
 * @author Cosimo Sguanci
 */

public class Hera extends GodStrategy {

    public static final String NAME = "Hera";
    public static final String DESCRIPTION = "Description";
    public static final String POWER_DESCRIPTION = "Power Description";

    @Override
    public Map<String, String> getGodInfo() {
        HashMap<String, String> info = new HashMap<>();
        info.put("name", NAME);
        info.put("description", DESCRIPTION);
        info.put("power_description", POWER_DESCRIPTION);
        return info;
    }


    /**
     * Implements Hera Win Constraints to other players.
     *
     * @see Hera#isPerimeterCell(Cell)
     * @param opponentWorker    the opponent selected worker for the current turn.
     * @param moveCell          the cell in which the opponent Player moved the selected worker.
     * @return true if the Cell where the opposite Worker moved is not a perimeter Cell, false otherwise
     */
    @Override
    public boolean checkWinConstraints(Worker opponentWorker, Cell moveCell) {
        return !isPerimeterCell(moveCell);
    }

    /**
     * Checks if the Cell passed is on the perimeter of the game Board.
     *
     * @param cell    the Cell in which the Opponent Worker was moved
     * @return true if the Cell is in the perimeter of the game Board, false otherwise.
     */
    private boolean isPerimeterCell(Cell cell) {
        return cell.getColIdentifier() == Board.WIDTH_SIZE - 1 || cell.getRowIdentifier() == Board.HEIGHT_SIZE - 1
                || cell.getColIdentifier() == 0 || cell.getRowIdentifier() == 0;
    }
}
