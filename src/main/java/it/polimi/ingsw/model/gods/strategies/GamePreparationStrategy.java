package it.polimi.ingsw.model.gods.strategies;

import it.polimi.ingsw.model.Worker;

public interface GamePreparationStrategy extends GodStrategy {
    void prepareGame(Worker workerFirst, Worker workerSecond);
}
