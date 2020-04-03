package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Worker;


public class Apollo implements GodStrategy {

    private final OpponentWorkerMoverDelegate opponentWorkerMoverDelegate;

    public Apollo() {
        this.opponentWorkerMoverDelegate = new OpponentWorkerMoverDelegate();
    }

    @Override
    public boolean checkMove(Worker worker, Cell moveCell) {
        return worker.getPosition().isAdjacentTo(moveCell)
                && worker.getPosition().isLevelDifferenceOk(moveCell)
                && moveCell.getLevel() != BlockType.DOME; // Apollo: all controls except moveCell.isEmpty()

    }

    @Override
    public boolean checkBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        return worker.standardCheckBuild(buildCell);
    }

    @Override
    public void executeMove(Worker worker, Cell moveCell) {

        if(!moveCell.isEmpty()) {
           opponentWorkerMoverDelegate.moveOpponentWorker(worker, moveCell.getWorker());
       }

       worker.move(moveCell);
    }

    @Override
    public void executeBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        worker.build(buildCell);
    }

    @Override
    public void prepareGame() {}

    @Override
    public boolean checkGamePreparation() {
        return true;
    }

    @Override
    public void endTurn(Match match) {}
}
