package it.polimi.ingsw.model.gods;

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
    public boolean checkMovement(Worker worker, Cell moveCell) {
        return standardCheckMovement(worker, moveCell) && multipleMovementDelegate.canMoveAgain() && !(moveCell.equals(previousCellNeededDelegate.getPreviousCell()));
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
        previousCellNeededDelegate.setPreviousCell(worker.getPosition());
        try {
            worker.move(moveCell);
            multipleMovementDelegate.increaseMoveCount();
        } catch (Exception e) {
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
        multipleMovementDelegate.reinitializeMoveCount();
        previousCellNeededDelegate.reinitializeCell();
    }
}
