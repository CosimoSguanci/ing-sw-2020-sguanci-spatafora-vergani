package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.gods.strategies.AnyLevelDomeBuilderStrategy;
import it.polimi.ingsw.model.gods.strategies.GamePreparationStrategy;
import it.polimi.ingsw.model.gods.strategies.GodStrategy;

public class Selene implements GamePreparationStrategy, AnyLevelDomeBuilderStrategy {
    @Override
    public boolean checkMovement(Worker worker, Cell moveCell) {
        return false;
    }

    @Override
    public boolean checkConstruction(Worker worker, Cell buildCell) {
        return false;
    }

    @Override
    public void buildDome(Cell cell) {

    }

    @Override
    public void prepareGame(Worker workerFirst, Worker workerSecond) {

    }
}
