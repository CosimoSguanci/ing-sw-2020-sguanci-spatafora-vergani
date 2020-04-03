package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Worker;

public class Artemis implements GodStrategy {
    final int ARTEMIS_MAX_MOVE_NUM = 2;
    private MultipleMovementDelegate multipleMovementDelegate;
    private PreviousCellNeededDelegate previousCellNeededDelegate;

    public Artemis() {
        this.multipleMovementDelegate = new MultipleMovementDelegate(ARTEMIS_MAX_MOVE_NUM);
        this.previousCellNeededDelegate = new PreviousCellNeededDelegate();
    }

    @Override
    public boolean checkMove(Worker worker, Cell moveCell) {
        return worker.standardCheckMove(moveCell) && multipleMovementDelegate.canMoveAgain() && !(moveCell.equals(previousCellNeededDelegate.getPreviousCell()));
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
        previousCellNeededDelegate.setPreviousCell(worker.getPosition());
        worker.move(moveCell);
        multipleMovementDelegate.increaseMoveCount();
    }

    @Override
    public void prepareGame() {}

    @Override
    public boolean checkGamePreparation() {
        return true;
    }

    @Override
    public void endTurn(Match match) {
        multipleMovementDelegate.reinitializeMoveCount();
        previousCellNeededDelegate.reinitializeCell();
    }
}
