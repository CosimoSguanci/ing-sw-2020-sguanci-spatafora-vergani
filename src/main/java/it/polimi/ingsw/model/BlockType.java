package it.polimi.ingsw.model;

/**
 * BlockType is the enumeration for the type of a block. In Santorini there are
 * four types of blocks: block of level one, block of level two, block of level three and
 * dome. Except particular conditions, a build increases the height of a tower, passing
 * from ground level to the first, then second, ... to a dome. No {@link Worker} can build over
 * a dome.
 * Ground level is part of the enumeration because it is convenient to know the height of
 * every cell, even the ones where no one has ever built during the game.
 *
 * @author Andrea Mario Vergani
 */
public enum BlockType {
    GROUND(0), LEVEL_ONE(1), LEVEL_TWO(2), LEVEL_THREE(3), DOME(4);

    /**
     * Integer representing the level, useful to make comparisons.
     */
    private final int levelNumber;

    /**
     * The constructor simply associates the enumeration with the level number
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

    /**
     * Static method used to convert a blockType to its CLI representation.
     *
     * @param level the BlockType whose CLI representation is requested
     * @return the level's CLI representation, to be showed in the command line View.
     */
    public static String convertBlockTypeToText(BlockType level) {
        switch (level) {
            case GROUND:
                return "0";
            case LEVEL_ONE:
                return "1";
            case LEVEL_TWO:
                return "2";
            case LEVEL_THREE:
                return "3";
            default:
                return "D";
        }
    }

}
