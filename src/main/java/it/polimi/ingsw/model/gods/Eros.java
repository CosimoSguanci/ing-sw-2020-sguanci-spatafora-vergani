package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.gods.strategies.GamePreparationStrategy;
import it.polimi.ingsw.model.gods.strategies.GodStrategy;

public class Eros implements GamePreparationStrategy {
    @Override
    public boolean checkMovement(Worker worker, Cell moveCell) {
        return false;
    }

    @Override
    public boolean checkConstruction(Worker worker, Cell buildCell) {
        return false;
    }

    @Override
    public void prepareGame(Worker workerFirst, Worker workerSecond) {

    }

    private boolean checkOppositeBorder(Worker workerFirst, Worker workerSecond) {
        return true;
    }
}
