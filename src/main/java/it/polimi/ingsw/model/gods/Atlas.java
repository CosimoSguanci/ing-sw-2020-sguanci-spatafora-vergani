package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;

/**
 * This class implements the Atlas strategy used by the Player who chose the powers of this God.
 * Specifically, an Atlas Worker can build a dome at any level.
 *
 * @author Cosimo Sguanci
 */

public class Atlas extends GodStrategy {

    public static final String NAME = "Atlas";
    public static final String DESCRIPTION = "Titan Shouldering the Heavens";
    public static final String POWER_DESCRIPTION = "Your Build: Your Worker may build a dome at any level.";

    public Atlas() {
        super(NAME, DESCRIPTION, POWER_DESCRIPTION);
    }


    @Override
    public boolean checkBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {

        if (buildCellBlockType != BlockType.DOME) {
            return super.checkBuild(worker, buildCell, buildCellBlockType);
        }

        return isUsingSelectedWorker(worker) && worker.standardCheckBuild(buildCell);
    }

    /**
     * This method overrides executeBuild using Cell setLevel method to force the construction of a Dome at any level.
     * If another level is being built, it forward the call to superclass executeBuild.
     *
     * @param worker    the worker who want to build a new level.
     * @param buildCell the cell in which the Player want to build a new level.
     * @see GodStrategy#executeMove(Worker, Cell)
     */
   /* @Override
    public void executeBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        if (buildCellBlockType == BlockType.DOME) {
            buildCell.setLevel(buildCellBlockType);
            worker.setHasBuilt();
        } else {
            super.executeBuild(worker, buildCell, buildCellBlockType);
        }

    } */
}
