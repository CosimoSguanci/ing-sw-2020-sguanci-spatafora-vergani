package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.gods.handlers.PreviousPositionHandler;
import it.polimi.ingsw.model.gods.strategies.GodStrategy;
import it.polimi.ingsw.model.gods.strategies.OpponentWorkerMoverStrategy;
import it.polimi.ingsw.model.gods.strategies.PreviousPositionStrategy;

public class Apollo implements OpponentWorkerMoverStrategy, PreviousPositionStrategy {

    PreviousPositionHandler previousPositionHandler;

    public Apollo() {
        this.previousPositionHandler = new PreviousPositionHandler();
    }

    @Override
    public boolean checkMovement(Worker worker, Cell moveCell) {
        setPreviousPosition(worker.getPosition());// maybe add another event? (callback)
        return true; // Apollo can always move to an adjacent cell
    }

    @Override
    public boolean checkConstruction(Worker worker, Cell buildCell) {
        return true;
    }

    @Override
    public void moveOpponentWorker(Worker opponentWorker) {

        try {
            opponentWorker.move(getPreviousPosition());
        } catch(Exception e) {
            e.printStackTrace();
        }
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
