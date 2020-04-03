package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Worker;

public class Zeus implements GodStrategy {
    @Override
    public boolean checkMove(Worker worker, Cell moveCell) {
        return worker.standardCheckMove(moveCell);
    }

    @Override
    public boolean checkBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        return (worker.getPosition().isAdjacentTo(buildCell) || buildCell.equals(worker.getPosition())) && (buildCell.getLevel() != BlockType.DOME);
    }

    @Override
    public void executeMove(Worker worker, Cell moveCell) {
        worker.move(moveCell);
    }

    @Override
    public void executeBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        worker.build(buildCell); //checkAndExecute ?
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

    }
}
