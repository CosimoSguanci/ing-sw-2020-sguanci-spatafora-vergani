package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;

import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the Zeus strategy used by the Player who chose the powers of this God.
 * Specifically, the selected Worker is allowed to build a block under itself.
 *
 * @author Cosimo Sguanci
 */

public class Zeus extends GodStrategy {

    public static final String NAME = "Zeus";
    public static final String DESCRIPTION = "God of the Sky";
    public static final String POWER_DESCRIPTION = "Your Build: Your Worker may build a block under itself.";

    @Override
    public Map<String, String> getGodInfo() {
        HashMap<String, String> info = new HashMap<>();
        info.put("name", NAME);
        info.put("description", DESCRIPTION);
        info.put("power_description", POWER_DESCRIPTION);
        return info;
    }


    /**
     * This method implements build check to also allow the construction of a block on the same position of the selected Worker.
     *
     * @param worker    the worker who want to build a new level.
     * @param buildCell the cell in which the Player want to build a new level.
     * @return true if the Build passed as parameter can be performed, false otherwise.
     */
    @Override
    public boolean checkBuild(Worker worker, Cell buildCell, BlockType buildCellBlockType) {
        return isUsingSelectedWorker(worker) && worker.hasMoved() && !worker.hasBuilt() && (worker.getPosition().isAdjacentTo(buildCell) || worker.getPosition().equals(buildCell)) && (buildCell.getLevel() != BlockType.DOME);
    }

}
