package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.gods.handlers.MultipleMovementHandler;
import it.polimi.ingsw.model.gods.handlers.PreviousPositionHandler;
import it.polimi.ingsw.model.gods.strategies.GodStrategy;
import it.polimi.ingsw.model.gods.strategies.MultipleBuildStrategy;
import it.polimi.ingsw.model.gods.strategies.MultipleMovementStrategy;
import it.polimi.ingsw.model.gods.strategies.PreviousPositionStrategy;

public class Artemis implements PreviousPositionStrategy, MultipleMovementStrategy {
    final int ARTEMIS_MAX_MOVE_NUM = 2;
    private MultipleMovementHandler multipleMovementHandler;
    private PreviousPositionHandler previousPositionHandler;

    public Artemis() {
        this.multipleMovementHandler = new MultipleMovementHandler(ARTEMIS_MAX_MOVE_NUM);
        this.previousPositionHandler = new PreviousPositionHandler();
    }

    @Override
    public boolean checkMovement(Worker worker, Cell moveCell) {
        setPreviousPosition(worker.getPosition());
        return canMoveAgain() && !(moveCell.equals(getPreviousPosition()));
    }

    @Override
    public boolean checkConstruction(Worker worker, Cell buildCell) {
        return true;
    }

    @Override
    public int getMoveCount() {
        return multipleMovementHandler.getMoveCount();
    }

    @Override
    public boolean canMoveAgain() {
        return multipleMovementHandler.canMoveAgain();
    }

    @Override
    public void increaseMoveCount() {
        multipleMovementHandler.increaseMoveCount();
    }

    @Override
    public Cell getPreviousPosition() {
        return previousPositionHandler.getPreviousPosition();
    }

    @Override
    public void setPreviousPosition(Cell previousPosition) {
        this.previousPositionHandler.setPreviousPosition(previousPosition);
    }
}
