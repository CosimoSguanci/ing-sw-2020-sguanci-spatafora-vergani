package it.polimi.ingsw.model.gods.handlers;

import it.polimi.ingsw.model.Cell;

public class PreviousPositionHandler {
    private Cell previousPosition;

    public void setPreviousPosition(Cell cell) {
        this.previousPosition = cell;
    }

    public Cell getPreviousPosition() {
        return this.previousPosition;
    }
}
