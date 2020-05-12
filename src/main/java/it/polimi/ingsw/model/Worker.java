package it.polimi.ingsw.model;


import it.polimi.ingsw.exceptions.CannotIncreaseLevelException;
import it.polimi.ingsw.exceptions.CellNotEmptyException;

/**
 *  This class contains information about the state of a single worker. Players in game
 *  have two different worker and every worker has the possibility to move and build on
 *  the Board. Workers are associated to a Player who can move them. Every worker has a
 *  different position (cell) on the board from the others and they are all on the same
 *  board.
 *  The class include methods to control the movement and construction of workers.
 *
 * @author Roberto Spatafora
 * @author Cosimo Sguanci
 */
public class Worker {
    public final Player player;
    private transient Cell position;
    private transient BlockType previousPositionBlockType;
    public final transient Board board;

    private transient boolean hasMoved;
    private transient boolean hasBuilt;

    public final String workerType;

    /**
     * Worker is the builder of the class. At the moment of a worker creation a player is
     * associated to it and the relative board in which the player is playing.
     *
     * @param player indicates the player who has the control on the worker being created.
     * @param board indicates the board table in which the player is involved in.
     */
    Worker(Player player, Board board, String workerType) {
        this.player = player;
        this.board = board;
        this.workerType = workerType;
    }

    /**
     * This method returns the cell in which worker is at the moment of invocation.
     * It returns a valid cell every time it is invoked after the first invocation
     * of setInitialPosition method.
     *
     * @return the cell in which the worker is, returns null if no cell is yet associated to it.
     * */
    public Cell getPosition() {
        return this.position;
    }

    public BlockType getPreviousPositionBlockType() {
        return this.previousPositionBlockType;
    }

    /**
     * This method sets the first cell of a worker
     *
     * @param row indicates the row position associated to a single worker at the beginning of the match
     * @param col indicates the column position associated to a single worker at the beginning of the match
     */
    public void setInitialPosition(int row, int col) throws CellNotEmptyException {
        if(board.getCell(row, col).isEmpty()) {
            this.position = board.getCell(row, col);
            this.board.getCell(row, col).setWorker(this);
        }
        else {
            throw new CellNotEmptyException();
        }
    }

    /**
     * This method implements the standard checks that have to be done if a Player wants to move a Worker
     * following the standard set of rules.
     *
     * @param moveCell the cell in which the Player want to move the worker.
     * @return true if (AND conditions):
     * - The Worker hasn't already performed a move or a build;
     * - The new Cell is adjacent to the current Worker's position;
     * - The new Cell is empty;
     * - The new Cell level is not DOME.
     */
    public boolean standardCheckMove(Cell moveCell) {
        if (!this.hasMoved && !this.hasBuilt && this.position.isAdjacentTo(moveCell) && (moveCell.isEmpty()) && (moveCell.getLevel() != BlockType.DOME)) {
            return this.position.isLevelDifferenceOk(moveCell);
        }
        return false;
    }

    /**
     * This method implements the standard checks that have to be done if a Player wants to use a Worker to build a block,
     * following the standard set of rules.
     *
     * @param buildCell the cell in which the Player want to build a new level.
     * @return true if (AND conditions):
     * - The Worker has already performed a move but NOT another build;
     * - The build Cell is adjacent to the current Worker's position;
     * - The build Cell is empty;
     * - The build Cell level is not DOME.
     */
    public boolean standardCheckBuild(Cell buildCell) {

        // Workers can build if the cell level is not the maximum and if the cell has not another Worker in it.
        return this.hasMoved && !this.hasBuilt && this.position.isAdjacentTo(buildCell) && (buildCell.getLevel() != BlockType.DOME) && (buildCell.isEmpty());
    }

    /**
     * This method allows worker to move to a cell that is specified as parameter.
     * Respecting the playing condition a worker can move to another cell only if
     * no other worker is on it and the cell is not at the maximum level it can
     * reach and the cell is adjacent to the cell the worker is at the moment of invocation.
     * The worker can move up at most one level.
     *
     * @param moveCell indicates the Cell in which a player wants his worker to move in.
     */
    /*
     * @throws Exception either a worker tries to move in a non adjacent cell or if the cell
     * is already occupied or has reached its maximum level. It also throws Exception if a
     * worker tries to move up more than a single level.
     */
    public void move(Cell moveCell) {

        if(this.position.getWorker().equals(this)) // todo OK ?
            this.position.setWorker(null);

        this.previousPositionBlockType = position.getLevel();
        this.position = moveCell;
        moveCell.setWorker(this);

        this.hasMoved = true;
    }

    /** This method allows worker to build into a cell that is specified as parameter.
     * Respecting the playing condition a worker can build in a cell only if
     * no other worker is on it and the cell is not at the maximum level it can
     * reach and the cell is adjacent to the cell the worker is at the moment of invocation.
     * As a result of this method the level of a cell is increased.
     *
     * @param buildCell indicates the Cell in which a player wants his worker to build in.
     */
    /*
     * @throws Exception either a worker tries to build in a non adjacent cell or if the cell
     * is already occupied or has reached its maximum level.
     */
    public void build(Cell buildCell, BlockType buildCellBlockType) throws CannotIncreaseLevelException, CellNotEmptyException {

        if(buildCellBlockType == null) {
            buildCell.increaseLevel();
        }
        else {
            buildCell.setLevel(buildCellBlockType);
        }

        this.hasBuilt = true;
    }

    public boolean hasMoved() {
        return this.hasMoved;
    }

    public boolean hasBuilt() {
        return this.hasBuilt;
    }

    public void setHasBuilt() {
        this.hasBuilt = true;
    }

    public void setHasMoved() {
        this.hasMoved = true;
    }

    public void reinitializeBuiltMoved() {
        this.hasMoved = false;
        this.hasBuilt = false;
    }

    public boolean canMove() {
        return this.board.movementPossibleFromCell(this.position);
    }

   /* public boolean canBuild() {

    } */

}