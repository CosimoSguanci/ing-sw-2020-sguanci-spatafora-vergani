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
    public boolean checkMove(Worker worker, Cell moveCell) {
        return worker.standardCheckMove(moveCell);
    }

    @Override
    public boolean checkBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        Worker unmovedWorker = worker.player.getWorkerFirst().equals(movedWorker) ?
                worker.player.getWorkerSecond() : worker.player.getWorkerFirst();
        if(unmovedWorker.getPosition().getLevel() == BlockType.GROUND)
            return worker.standardCheckBuild(buildCell) && multipleBuildDelegate.canBuildAgain();

        return worker.standardCheckBuild(buildCell);
    }

    @Override
    public void executeMove(Worker worker, Cell moveCell) {
        worker.move(moveCell);
        movedWorker = worker;
    }

    @Override
    public void executeBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        worker.build(buildCell);
        multipleBuildDelegate.increaseBuildCount();
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
