package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Worker;

public class Athena implements GodStrategy {

    private boolean shouldBlockLevelUp;

    @Override
    public boolean checkBuild(Worker worker, Cell buildCell) {
        return standardCheckBuild(worker, buildCell);
    }

    @Override
    public boolean checkMovement(Worker worker, Cell moveCell) {
        return standardCheckMovement(worker, moveCell);
    }

    @Override
    public void executeMovement(Worker worker, Cell moveCell) {
        try {
            Cell prev = worker.getPosition();
            worker.move(moveCell);
            shouldBlockLevelUp = moveCell.levelDifference(prev) == 1;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executeBuild(Worker worker, Cell buildCell) {
        try {
            worker.build(buildCell);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void prepareGame() {}

    @Override
    public boolean checkGamePreparation() {
        return true;
    }

    @Override
    public void endTurn(Match match) {
        match.setCanMove(!shouldBlockLevelUp);
    }
}
