package it.polimi.ingsw.model.gods.handlers;

public class MultipleMovementHandler {
    final int MAX_MOVE_COUNT;
    private int moveCount;

    public MultipleMovementHandler(int maxMoveCount) {
        this.MAX_MOVE_COUNT = maxMoveCount;
    }

    public int getMoveCount() {
        return moveCount;
    }
    public boolean canMoveAgain() {
        return moveCount < MAX_MOVE_COUNT;
    }
    public void increaseMoveCount() {this.moveCount++;}
}
