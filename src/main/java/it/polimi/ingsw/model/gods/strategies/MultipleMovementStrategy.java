package it.polimi.ingsw.model.gods.strategies;

public interface MultipleMovementStrategy extends GodStrategy{
    int getMoveCount();
    void increaseMoveCount();
    boolean canMoveAgain();
}
