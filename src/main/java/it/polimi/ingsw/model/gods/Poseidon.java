package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.gods.handlers.MultipleBuildHandler;
import it.polimi.ingsw.model.gods.strategies.GodStrategy;
import it.polimi.ingsw.model.gods.strategies.MultipleBuildStrategy;

public class Poseidon implements MultipleBuildStrategy {
    final int POSEIDON_MAX_BUILD_NUM = 3;
    private MultipleBuildHandler multipleBuildHandler;

    public Poseidon() {
        multipleBuildHandler = new MultipleBuildHandler(POSEIDON_MAX_BUILD_NUM);
    }

    @Override
    public boolean checkMovement(Worker worker, Cell moveCell) {
        return false;
    }

    @Override
    public boolean checkConstruction(Worker worker, Cell buildCell) {
        return false;
    }

    @Override
    public int getBuildCount() {
        return multipleBuildHandler.getBuildCount();
    }

    @Override
    public boolean canBuildAgain() {
        return multipleBuildHandler.canBuildAgain();
    }

    @Override
    public void increaseBuildCount() {
        multipleBuildHandler.increaseBuildCount();
    }
}