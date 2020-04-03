package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Worker;

public class Hephaestus implements GodStrategy {
    final int HEPHAEUSTUS_MAX_BUILD_NUM = 2;
    private MultipleBuildDelegate multipleBuildDelegate;
    private PreviousCellNeededDelegate previousCellNeededDelegate; // used to save the previous cell where Atlas built a level

    public Hephaestus() {
        multipleBuildDelegate = new MultipleBuildDelegate(HEPHAEUSTUS_MAX_BUILD_NUM);
        previousCellNeededDelegate = new PreviousCellNeededDelegate();
    }

    @Override
    public boolean checkMove(Worker worker, Cell moveCell) {
        return worker.standardCheckMove(moveCell);
    }

    @Override
    public boolean checkBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {

        if(!multipleBuildDelegate.canBuildAgain())
            return false;

        if(multipleBuildDelegate.getBuildCount() == 0)
            return worker.standardCheckBuild(buildCell);

        Cell prevBuildCell = previousCellNeededDelegate.getPreviousCell();

        return buildCell.equals(prevBuildCell) && buildCellBlockType != BlockType.DOME && worker.standardCheckBuild(buildCell);
    }

    @Override
    public void executeMove(Worker worker, Cell moveCell) {
        worker.move(moveCell);
    }

    @Override
    public void executeBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        worker.build(buildCell);
        previousCellNeededDelegate.setPreviousCell(buildCell);
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
        previousCellNeededDelegate.reinitializeCell();
    }
}
