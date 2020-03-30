package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.Cell;

class PreviousCellNeededDelegate {
    private Cell previousCell;

    void setPreviousPosition(Cell cell) {
        this.previousCell = cell;
    }

    Cell getPreviousCell() {
        return this.previousCell;
    }

    void reinitializeCell() {this.previousCell = null;}
}
