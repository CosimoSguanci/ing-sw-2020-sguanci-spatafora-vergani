package it.polimi.ingsw.model;

/**
 *  This class contains information about the state of a single worker. Players in game
 *  have two different worker and every worker has the possibility to move and build on
 *  the Board. Workers are associated to a Player who can move them. Every worker has a
 *  different position (cell) on the board from the others and they are all on the same
 *  board.
 *  The class include methods to control the movement and construction of workers.
 *
 * @author Roberto Spatafora *
 */
public class Worker {
    public final Player player;
    private Cell position;
    public final Board board;

    /**
     * Worker is the builder of the class. At the moment of a worker creation a player is
     * associated to it and the relative board in which the player is playing.
     *
     * @param player indicates the player who has the control on the worker being created.
     * @param board indicates the board table in which the player is involved in.
     */
    public Worker(Player player, Board board) { //Alternatively, we can give as parameter to constructor the firstPosition
        this.player = player;
        this.board = board;
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

    /**
     * This method sets the first cell of a worker
     *
     * @param row indicates the row position associated to a single worker at the beginning of the match
     * @param col indicates the column position associated to a single worker at the beginning of the match
     */
    public void setInitialPosition(int row, int col) throws Exception {
        if(board.getCell(row, col).isEmpty()) {
            this.position = board.getCell(row, col);
        }
        else { throw new Exception(); } //cellAlreadyOccupiedException();
    }

    /**
     * This method allows worker to move to a cell that is specified as parameter.
     * Respecting the playing condition a worker can move to another cell only if
     * no other worker is on it and the cell is not at the maximum level it can
     * reach and the cell is adjacent to the cell the worker is at the moment of invocation.
     * The worker can move up at most one level.
     *
     * @param newCell indicates the Cell in which a player wants his worker to move in.
     *
     * @throws Exception either a worker tries to move in a non adjacent cell or if the cell
     * is already occupied or has reached its maximum level. It also throws Exception if a
     * worker tries to move up more than a single level.
     */
    public void move(Cell newCell) throws Exception {
        if (this.position.isAdjacentTo(newCell) && (newCell.isEmpty()) && (newCell.getLevel() != BlockType.DOME)) {
            if(this.player.match.getCanMove()) {
                if (newCell.levelDifference(this.position) <= 1) { // Moving into a DOME is not allowed.
                    this.position.setWorker(this);
                    this.position = newCell;
                } else {throw new Exception(); } //else { throw new InvalidMoveException(); }
            }
        } else {throw new Exception(); } // else { throw new InvalidCellException(); }
    }

    /** This method allows worker to build into a cell that is specified as parameter.
     * Respecting the playing condition a worker can build in a cell only if
     * no other worker is on it and the cell is not at the maximum level it can
     * reach and the cell is adjacent to the cell the worker is at the moment of invocation.
     * As a result of this method the level of a cell is increased.
     *
     * @param cell indicates the Cell in which a player wants his worker to build in.
     *
     * @throws Exception either a worker tries to build in a non adjacent cell or if the cell
     * is already occupied or has reached its maximum level.
     */
    public void build(Cell cell) throws Exception {
        if (this.position.isAdjacentTo(cell) && (cell.getLevel() != BlockType.DOME) && (cell.isEmpty())) {// Workers can build if the cell level is not the maximum and if the cell has not another Worker in it.
            cell.increaseLevel();
        }  else {throw new Exception();} //{ throw new InvalidCellException(); }
    }

}
