package it.polimi.ingsw.model.gods.strategies;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;

public interface GodStrategy  {
    boolean checkMovement(Worker worker, Cell moveCell);
    boolean checkConstruction(Worker worker, Cell buildCell);
}