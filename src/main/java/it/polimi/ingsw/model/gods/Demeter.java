package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.gods.handlers.MultipleBuildHandler;
import it.polimi.ingsw.model.gods.handlers.PreviousPositionHandler;
import it.polimi.ingsw.model.gods.strategies.GodStrategy;
import it.polimi.ingsw.model.gods.strategies.MultipleBuildStrategy;
import it.polimi.ingsw.model.gods.strategies.PreviousPositionStrategy;

public class Demeter implements PreviousPositionStrategy, MultipleBuildStrategy {
    final int DEMETER_MAX_BUILD_NUM = 2;
    private MultipleBuildHandler multipleBuildHandler;
    private PreviousPositionHandler previousPositionHandler;

    public Demeter() {
        multipleBuildHandler = new MultipleBuildHandler(DEMETER_MAX_BUILD_NUM);
        previousPositionHandler = new PreviousPositionHandler();
    }

    @Override
    public boolean checkMovement(Worker worker, Cell moveCell) {
        return true;
    }

    @Override
    public boolean checkConstruction(Worker worker, Cell buildCell) {
        return canBuildAgain() && !(getPreviousPosition().equals(buildCell));
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

    @Override
    public Cell getPreviousPosition() {
        return previousPositionHandler.getPreviousPosition();
    }

    @Override
    public void setPreviousPosition(Cell previousPosition) {
        previousPositionHandler.setPreviousPosition(previousPosition);
    }
}
