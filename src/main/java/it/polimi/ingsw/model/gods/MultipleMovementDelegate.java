package it.polimi.ingsw.model.gods;

class MultipleMovementDelegate {
    private final int MAX_MOVE_COUNT;
    private int moveCount;

    MultipleMovementDelegate(int maxMoveCount) {
        this.MAX_MOVE_COUNT = maxMoveCount;
    }

    int getMoveCount() {
        return moveCount;
    }
    boolean canMoveAgain() {
        return moveCount < MAX_MOVE_COUNT;
    }
    void increaseMoveCount() {this.moveCount++;}
    void reinitializeMoveCount(){this.moveCount = 0;}
}
