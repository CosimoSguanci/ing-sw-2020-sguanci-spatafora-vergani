package it.polimi.ingsw.model.gods.strategies;

import it.polimi.ingsw.model.Worker;

public interface OpponentWorkerMoverStrategy extends GodStrategy{
    void moveOpponentWorker(Worker worker);
}
