package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Worker;

public class Hestia implements GodStrategy {
    final int HESTIA_MAX_BUILD_NUM = 2;
    private MultipleBuildDelegate multipleBuildDelegate;

    public Hestia() {
        multipleBuildDelegate = new MultipleBuildDelegate(HESTIA_MAX_BUILD_NUM);
    }

    @Override
    public boolean checkMovement(Worker worker, Cell moveCell) {
        return standardCheckMovement(worker, moveCell);
    }

    @Override
    public boolean checkBuild(Worker worker, Cell buildCell) {

        if(!standardCheckBuild(worker, buildCell) || !multipleBuildDelegate.canBuildAgain())
            return false;
        return multipleBuildDelegate.getBuildCount() != HESTIA_MAX_BUILD_NUM - 1 || !isPerimeterCell(buildCell);

    }

    @Override
    public void executeBuild(Worker worker, Cell buildCell) {
        try {
            worker.move(buildCell);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executeMovement(Worker worker, Cell moveCell) {
        try {
            worker.move(moveCell);
        } catch(Exception e) {
            e.printStackTrace();
        }
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
        return cell.getColIdentifier() == Board.WIDTH_SIZE - 1 || cell.getRowIdentifier() == Board.HEIGHT_SIZE;
    }
}
