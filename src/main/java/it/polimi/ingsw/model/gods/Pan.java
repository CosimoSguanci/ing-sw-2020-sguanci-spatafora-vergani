package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.gods.strategies.GodStrategy;

public class Pan implements GodStrategy {
    @Override
    public boolean checkMovement(Worker worker, Cell moveCell) {
        return false;
    }

    @Override
    public boolean checkConstruction(Worker worker, Cell buildCell) {
        return false;
    }
}
