package it.polimi.ingsw.model.gods.strategies;

import it.polimi.ingsw.model.Cell;

public interface PreviousPositionStrategy extends GodStrategy{
     Cell getPreviousPosition();
     void setPreviousPosition(Cell previousPosition);
}
