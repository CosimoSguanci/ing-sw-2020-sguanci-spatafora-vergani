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
    public static final String POWER_DESCRIPTION = "Your Build: Your Worker may build a dome at any level";

    public Atlas() {
        super(NAME, DESCRIPTION, POWER_DESCRIPTION);
    }

    /**
     * If the level being built is not a dome, this method calls the standard superclass checkBuild.
     * Otherwise, it performs all the standard checks excepts the controls about the fact that the
     * level being built should the existing level + 1.
     *
     * @param worker    the worker who want to build a new level.
     * @param buildCell the cell in which the Player want to build a new level.
     * @return true if the Build passed as parameter can be performed, false otherwise.
     * @see GodStrategy#checkBuild(Worker, Cell, BlockType)
     */
    @Override
    public boolean checkBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {

        if (buildCellBlockType != BlockType.DOME) {
            return super.checkBuild(worker, buildCell, buildCellBlockType);
        }

        return isUsingSelectedWorker(worker) &&
                worker.hasMoved() &&
                !worker.hasBuilt() &&
                worker.getPosition().isAdjacentTo(buildCell) &&
                buildCell.getLevel() != BlockType.DOME &&
                buildCell.isEmpty();
    }
}
