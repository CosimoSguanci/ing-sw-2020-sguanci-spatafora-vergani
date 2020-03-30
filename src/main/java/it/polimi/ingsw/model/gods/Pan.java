package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Worker;

public class Pan implements GodStrategy {

    private boolean downTwoLevels;

    @Override
    public boolean checkMovement(Worker worker, Cell moveCell) {
        return standardCheckMovement(worker, moveCell);
    }

    @Override
    public boolean checkBuild(Worker worker, Cell buildCell) {
        return standardCheckBuild(worker, buildCell);
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
    public void executeMovement(Worker worker, Cell moveCell) {
        try {
            Cell prev = worker.getPosition();
            worker.move(moveCell);
            downTwoLevels = prev.levelDifference(moveCell) >= 2;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void prepareGame() {

    }

    @Override
    public boolean checkGamePreparation() {
        return true;
    }

    @Override
    public void endTurn(Match match) {
        if(downTwoLevels) {
            // trigger win conditions
        }
    }
}
