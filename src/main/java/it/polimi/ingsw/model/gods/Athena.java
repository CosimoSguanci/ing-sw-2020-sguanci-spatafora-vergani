package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.gods.strategies.GodStrategy;

public class Athena implements GodStrategy {
    @Override
    public boolean checkConstruction(Worker worker, Cell buildCell) {
        return true;
    }

    @Override
    public boolean checkMovement(Worker worker, Cell moveCell) {
        return true;
    }
}
