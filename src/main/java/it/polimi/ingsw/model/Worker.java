package it.polimi.ingsw.model;

public class Worker {
    public final Player player;
    private Cell position;
    private Board board;

    public Worker(Player player) { //Alternatively, we can give as parameter to constructor the firstPosition
        this.player = player;
    }

    public Cell getPosition() {
        return this.position;
    }

    public void setInitialPosition(Cell initialPosition) {
        this.position = initialPosition;
    }

    public void move(Cell newCell) {
        if (board.isAdjacentTo(this.position, newCell)) {
            if (newCell.levelDifference(this.position) <= 1) {
                this.position = newCell;
            } //else { throw new InvalidMoveException(); }
        } // else { throw new InvalidCellException(); }
    }

    public void build(Cell cell) {
        if (board.isAdjacentTo(this.position, cell)) {
            if (! cell.getLevel().equals("DOME") ) {
                cell.increaseLevel();
            } // else { throw new InvalidBuildException() }
        } // else { throw new InvalidCellException(); }
    }

}
