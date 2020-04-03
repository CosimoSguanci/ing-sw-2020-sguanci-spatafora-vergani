package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Worker;

public class Pan implements GodStrategy { // handle flag(s) win conditions

    private boolean downTwoLevels;

    @Override
    public boolean checkMove(Worker worker, Cell moveCell) {
        return worker.standardCheckMove(moveCell);
    }

    @Override
    public boolean checkBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        return worker.standardCheckBuild(buildCell);
    }

    @Override
    public void executeBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        worker.build(buildCell);
    }

    @Override
    public void executeMove(Worker worker, Cell moveCell) {
        Cell prev = worker.getPosition();
        worker.move(moveCell);
        downTwoLevels = prev.levelDifference(moveCell) >= 2;
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
