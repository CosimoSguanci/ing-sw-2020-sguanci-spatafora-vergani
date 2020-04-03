package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;

public class Hestia implements GodStrategy {
    final int HESTIA_MAX_BUILD_NUM = 2;
    private MultipleBuildDelegate multipleBuildDelegate;

    public Hestia() {
        multipleBuildDelegate = new MultipleBuildDelegate(HESTIA_MAX_BUILD_NUM);
    }

    @Override
    public boolean checkMove(Worker worker, Cell moveCell) {
        return worker.standardCheckMove(moveCell);
    }

    @Override
    public boolean checkBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {

        if(!worker.standardCheckBuild(buildCell) || !multipleBuildDelegate.canBuildAgain())
            return false;
        return multipleBuildDelegate.getBuildCount() != HESTIA_MAX_BUILD_NUM - 1 || !isPerimeterCell(buildCell);

    }

    @Override
    public void executeBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        worker.build(buildCell);
    }

    @Override
    public void executeMove(Worker worker, Cell moveCell) {
        worker.move(moveCell);
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

    private boolean isPerimeterCell(Cell cell) {
        return cell.getColIdentifier() == Board.WIDTH_SIZE - 1 || cell.getRowIdentifier() == Board.HEIGHT_SIZE - 1;
    }
}
