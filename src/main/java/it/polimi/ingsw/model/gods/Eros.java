package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;

/**
 * This class implements the Eros strategy used by the Player who chose the powers of this God.
 * Specifically, Eros imposes a Game Preparation constraints: the Player's worker must be placed
 * at opposite borders when the match starts. Moreover, it has some additional Win Conditions: Eros Player
 * wins also if one workers moves to a position adjacent to the other (unmoved) worker's position
 * (they must also be both on the level one, or the same level in a 3-Players Match).
 *
 * @author Cosimo Sguanci
 */

public class Eros extends GodStrategy {

    public static final String NAME = "Eros";
    public static final String DESCRIPTION = "God of Desire";
    public static final String POWER_DESCRIPTION = "Setup: Place your Workers anywhere along opposite edges of the board\n" +
            "Win Condition: You also win if one of your Workers moves to a space neighboring your other Worker and both are on the first level (or the same level in a 3-player game)";

    /**
     * Flag used to check the additional Win Condition.
     */
    private boolean neighboringOtherWorker;

    public Eros() {
        super(NAME, DESCRIPTION, POWER_DESCRIPTION);
    }


    /**
     * This method checks that Eros Game Preparation constraints are satisfied: the workers must be at opposite sides of the Match board.
     *
     * @param workerFirst      the "first" Player Worker.
     * @param workerFirstCell  the cell in which the Player wants to put the "first" Worker at startup.
     * @param workerSecond     the "second" Player Worker.
     * @param workerSecondCell the cell in which the Player wants to put the "second" Worker at startup.
     * @return true if the Game Preparation constraints are satisfied, false otherwise.
     * @see Eros#checkOppositeBorder(Cell, Cell)
     */
    @Override
    public boolean checkGamePreparation(Worker workerFirst, Cell workerFirstCell, Worker workerSecond, Cell workerSecondCell) {
        return super.checkGamePreparation(workerFirst, workerFirstCell, workerSecond, workerSecondCell) && checkOppositeBorder(workerFirstCell, workerSecondCell);
    }

    /**
     * This method checks that the workers given as parameter are at opposite border of the game Board.
     *
     * @param workerFirstCell  the cell in which the Player wants to put the "first" Worker.
     * @param workerSecondCell the cell in which the Player wants to put the "second" Worker.
     * @return true if the Cells are at opposite borders of the game Board, false otherwise.
     */
    private boolean checkOppositeBorder(Cell workerFirstCell, Cell workerSecondCell) {
        return Math.abs(workerFirstCell.getRowIdentifier() - workerSecondCell.getRowIdentifier()) == Board.WIDTH_SIZE - 1
                || Math.abs(workerFirstCell.getColIdentifier() - workerSecondCell.getColIdentifier()) == Board.HEIGHT_SIZE - 1;
    }

    /**
     * This method first calls superclass execute move to actually move the worker.
     * Then it sets the flag that represents Eros Win Condition.
     *
     * @param worker   the worker that the Player wants to move.
     * @param moveCell the cell in which the Player want to move the worker.
     * @see GodStrategy#executeMove(Worker, Cell)
     */
    @Override
    public void executeMove(Worker worker, Cell moveCell) {
        super.executeMove(worker, moveCell);
        Worker otherWorker = worker.player.getWorkerFirst().equals(worker) ? worker.player.getWorkerSecond() : worker.player.getWorkerFirst();
        Match match = worker.player.getMatch();

        boolean levelCondition = match.getPlayersNumber() == 3 ? worker.getPosition().getLevel() == otherWorker.getPosition().getLevel()
                : (worker.getPosition().getLevel() == otherWorker.getPosition().getLevel() && worker.getPosition().getLevel() == BlockType.LEVEL_ONE);

        neighboringOtherWorker = worker.getPosition().isAdjacentTo(otherWorker.getPosition()) && levelCondition;

    }

    /**
     * This method checks if standard Win Conditions or Eros Win Conditions are satisfied.
     *
     * @param worker the worker that the Player selected for this turn.
     * @return true if Eros Wind Conditions OR standard Win Conditions are satisfied, false otherwise.
     * @see GodStrategy#checkWinCondition(Worker)
     */
    @Override
    public boolean checkWinCondition(Worker worker) {
        return super.checkWinCondition(worker) || neighboringOtherWorker;
    }

    /**
     * Calls superclass endTurn and resets flags to false.
     *
     * @param player The player whose turn is ending.
     * @see GodStrategy#endPlayerTurn(Player)
     */
    @Override
    public void endPlayerTurn(Player player) {
        super.endPlayerTurn(player);
        this.neighboringOtherWorker = false;
    }
}
