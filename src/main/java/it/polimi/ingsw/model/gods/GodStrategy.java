package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Worker;

public interface GodStrategy  {

    default boolean standardCheckMovement(Worker worker, Cell moveCell) {
        return worker.getPosition().isAdjacentTo(moveCell)
                && moveCell.isEmpty()
                && worker.getPosition().levelDifference(moveCell) >= -1
                && moveCell.getLevel() != BlockType.DOME;
    }

    default boolean standardCheckBuild(Worker worker, Cell buildCell) {
        return worker.getPosition().isAdjacentTo(buildCell)
                && buildCell.getLevel() != BlockType.DOME;
    }

    void prepareGame(); // called before game preparation
    boolean checkGamePreparation(); // called after game preparation
    boolean checkMovement(Worker worker, Cell moveCell);
    boolean checkBuild(Worker worker, Cell buildCell);
    void executeMovement(Worker worker, Cell moveCell);
    void executeBuild(Worker worker, Cell buildCell);
    void endTurn(Match match);
}