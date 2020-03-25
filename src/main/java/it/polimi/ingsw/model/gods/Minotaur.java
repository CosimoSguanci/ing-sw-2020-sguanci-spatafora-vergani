package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.gods.strategies.GodStrategy;
import it.polimi.ingsw.model.gods.strategies.OpponentWorkerMoverStrategy;

public class Minotaur implements OpponentWorkerMoverStrategy {

    @Override
    public boolean checkMovement(Worker worker, Cell moveCell) {
        return false;
    }

    @Override
    public boolean checkConstruction(Worker worker, Cell buildCell) {
        return false;
    }

    @Override
    public void moveOpponentWorker(Worker worker) {

    }
}
