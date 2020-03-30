package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;

public class Poseidon implements GodStrategy {
    final int POSEIDON_MAX_BUILD_NUM = 3;
    private MultipleBuildDelegate multipleBuildDelegate;
    private Worker movedWorker;

    public Poseidon() {
        multipleBuildDelegate = new MultipleBuildDelegate(POSEIDON_MAX_BUILD_NUM);
    }

    @Override
    public boolean checkMovement(Worker worker, Cell moveCell) {
        return standardCheckMovement(worker, moveCell);
    }

    @Override
    public boolean checkBuild(Worker worker, Cell buildCell) {
        Worker unmovedWorker = worker.player.getWorkerFirst().equals(movedWorker) ?
                worker.player.getWorkerSecond() : worker.player.getWorkerFirst();
        if(unmovedWorker.getPosition().getLevel() == BlockType.GROUND)
            return standardCheckBuild(worker, buildCell) && multipleBuildDelegate.canBuildAgain();

        return standardCheckBuild(worker, buildCell);
    }

    @Override
    public void executeMovement(Worker worker, Cell moveCell) {
        try {
            worker.move(moveCell);
            movedWorker = worker;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executeBuild(Worker worker, Cell buildCell) {
        try {
            worker.build(buildCell);
            multipleBuildDelegate.increaseBuildCount();
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
        multipleBuildDelegate.reinitializeBuildCount();
    }
}
