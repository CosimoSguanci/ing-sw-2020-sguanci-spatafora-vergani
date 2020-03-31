package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Worker;

public class Minotaur implements GodStrategy {

    private final OpponentWorkerMoverDelegate opponentWorkerMoverDelegate;

    public Minotaur() {
        this.opponentWorkerMoverDelegate = new OpponentWorkerMoverDelegate();
    }

    @Override
    public boolean checkMovement(Worker worker, Cell moveCell) {
        if(!(worker.getPosition().isAdjacentTo(moveCell)
                && worker.getPosition().isLevelDifferenceOk(moveCell)
                && moveCell.getLevel() != BlockType.DOME))
            return false;
        if(!moveCell.isEmpty()) {
            return true; // NEED TO IMPLEMENT BOARD SINGLETON
        }

        return true;
    }

    @Override
    public boolean checkBuild(Worker worker, Cell buildCell) {
        return false;
    }

    @Override
    public void executeMovement(Worker worker, Cell moveCell) {

    }

    @Override
    public void executeBuild(Worker worker, Cell buildCell) {

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
