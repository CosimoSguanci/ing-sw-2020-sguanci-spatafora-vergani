package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Worker;

public class Athena implements GodStrategy {

    private boolean shouldBlockLevelUp;

    @Override
    public boolean checkBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        return worker.standardCheckBuild(buildCell);
    }

    @Override
    public boolean checkMove(Worker worker, Cell moveCell) {
        return worker.standardCheckMove(moveCell);
    }

    @Override
    public void executeMove(Worker worker, Cell moveCell) {
        Cell prev = worker.getPosition();
        worker.move(moveCell);
        shouldBlockLevelUp = (moveCell.levelDifference(prev) == 1);
    }

    @Override
    public void executeBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
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
