package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Worker;

public class Atlas implements GodStrategy {
    @Override
    public boolean checkMovement(Worker worker, Cell moveCell) {
        return standardCheckMovement(worker, moveCell);
    }

    @Override
    public boolean checkBuild(Worker worker, Cell buildCell) {
        return worker.getPosition().isAdjacentTo(buildCell);
    }

    @Override
    public void executeBuild(Worker worker, Cell buildCell) {
        try {
            worker.build(buildCell); // handle domes, maybe pass PlayerCommand as parameter? Or Block Type
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executeMovement(Worker worker, Cell moveCell) {

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

    /*public void buildDome(Cell cell) {
        cell.setLevel(BlockType.DOME);
    }*/
}
