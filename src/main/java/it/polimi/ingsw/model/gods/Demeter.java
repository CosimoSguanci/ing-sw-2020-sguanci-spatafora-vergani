package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Worker;


public class Demeter implements GodStrategy {
    final int DEMETER_MAX_BUILD_NUM = 2;
    private MultipleBuildDelegate multipleBuildDelegate;
    private PreviousCellNeededDelegate previousCellNeededDelegate;

    public Demeter() {
        multipleBuildDelegate = new MultipleBuildDelegate(DEMETER_MAX_BUILD_NUM);
        previousCellNeededDelegate = new PreviousCellNeededDelegate();
    }

    @Override
    public boolean checkMove(Worker worker, Cell moveCell) {
        return worker.standardCheckMove(moveCell);
    }

    @Override
    public boolean checkBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        return worker.standardCheckBuild(buildCell) && multipleBuildDelegate.canBuildAgain() && !(previousCellNeededDelegate.getPreviousCell().equals(buildCell));
    }

    @Override
    public void executeBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        worker.build(buildCell);
        previousCellNeededDelegate.setPreviousCell(buildCell);
    }

    @Override
    public void executeMove(Worker worker, Cell moveCell) {
        worker.move(moveCell);
    }

    @Override
    public void prepareGame() {}

    @Override
    public boolean checkGamePreparation() {
        return true;
    }

    @Override
    public void endTurn(Match match) {
        multipleBuildDelegate.reinitializeBuildCount();
    }
}
