package it.polimi.ingsw.model;

/**
 * BlockType is the enumeration for the type of a block. In Santorini there are
 * four types of blocks: block of level one, block of level two, block of level three and
 * dome. Except particular conditions, a build increases the height of a tower, passing
 * from ground level to the first, then second, ... to a dome. No worker can build over
 * a dome.
 * Ground level is part of the enumeration because it is convenient to know the height of
 * every cell, even the ones where no one has ever built during the game.
 *
 * @author Andrea Mario Vergani
 */
public enum BlockType {
    GROUND(0), LEVEL_ONE(1), LEVEL_TWO(2), LEVEL_THREE(3), DOME(4);
    private int levelNumber;

    /**
     * The constructor simply associates the enumeration with levelNumber
     *
     * @param levelNumber is the int value associated with different levels
     */
    BlockType(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    /**
     * The method is getter for levelNumber
     *
     * @return the value of attribute called 'levelNumber', associated one-by-one with BlockType level
     */
    public int getLevelNumber() {
        return this.levelNumber;
    }

}
