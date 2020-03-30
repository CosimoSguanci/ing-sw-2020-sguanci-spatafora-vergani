package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Worker;

public class Hephaestus implements GodStrategy {
    final int HEPHAEUSTUS_MAX_BUILD_NUM = 2;
    private MultipleBuildDelegate multipleBuildDelegate;
    private PreviousCellNeededDelegate previousCellNeededDelegate;

    public Hephaestus() {
        multipleBuildDelegate = new MultipleBuildDelegate(HEPHAEUSTUS_MAX_BUILD_NUM);
        previousCellNeededDelegate = new PreviousCellNeededDelegate();
    }

    @Override
    public boolean checkMovement(Worker worker, Cell moveCell) {
        return standardCheckMovement(worker, moveCell);
    }

    @Override
    public boolean checkBuild(Worker worker, Cell buildCell) {
        return standardCheckBuild(worker, buildCell) && multipleBuildDelegate.canBuildAgain(); // add domes handling
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
    public void executeBuild(Worker worker, Cell buildCell) {
        try {
            worker.move(buildCell);
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
}
