package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;

/**
 * This class implements the Atlas strategy used by the Player who chose the powers of this God.
 * Specifically, an Atlas Worker can build a dome at any level.
 *
 * @author Cosimo Sguanci
 */

public class Atlas extends GodStrategy {

    // TODO SHOULD CHECK ALSO THE LEVEL BEING BUILT?
    // NOT NECESSARY BECAUSE standardaCheckBuild doesn't care about the level being built, but it consider the next level,
    // that in case of Atlas can always be also a DOME, TODO REMOVE METHOD
    /**
     * This method is called at every turn before executing the worker's build phase.
     * It checks if the build action which the player requested to do is allowed by the player
     * God's powers.
     *
     * //@param worker    the worker who want to build a new level.
     * //@param buildCell the cell in which the Player want to build a new level.
     */
   /* @Override
    public boolean checkBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        if (!isUsingSelectedWorker(worker))
            return false;

        return buildCellBlockType == BlockType.DOME
                ?
                worker.hasMoved() && !worker.hasBuilt() && worker.getPosition().isAdjacentTo(buildCell) && (buildCell.getLevel() != BlockType.DOME) && (buildCell.isEmpty())

                :
                worker.standardCheckBuild(buildCell);

    }*/

    /**
     * This method overrides executeBuild using Cell setLevel method to force the construction of a Dome at any level.
     * If another level is being built, it forward the call to superclass executeBuild.
     *
     * @see GodStrategy#executeMove(Worker, Cell)
     * @param worker    the worker who want to build a new level.
     * @param buildCell the cell in which the Player want to build a new level.
     */
    @Override
    public void executeBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        if(buildCellBlockType == BlockType.DOME) {
            buildCell.setLevel(buildCellBlockType);
            worker.setHasBuilt();
        }
        else {
            super.executeBuild(worker, buildCell, buildCellBlockType);
        }

    }
}
