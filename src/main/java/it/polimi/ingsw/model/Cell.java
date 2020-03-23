package it.polimi.ingsw.model;

/**
 * This class contains information about the state of a single cell. In the game, there
 * are 25 cells and the workers have the possibility to move adn build. Every cell has
 * an actual level (ground, first, second, third or dome), which can change during the
 * match; one worker can be on a cell, otherwise this one is "empty"
 *
 * @author Andrea Mario Vergani
 */
public class Cell {
    private BlockType level;
    private Worker worker;


    /**
     * Cell is the builder of the class. When a cell is created, its level is the ground
     * level and no worker is on it
     */
    public Cell() {
        this.level = BlockType.GROUND;
        this.worker = null;
    }

    /**
     * The method returns 'true' if no worker is on the considered cell, so this one is
     * "empty". Otherwise, the return value is 'false'
     *
     * @return true if the cell is empty, as explained above
     */
    public boolean isEmpty() {
        return this.worker == null;
    }

    /**
     * The method is a simple getter of the worker on the considered cell. If there is no
     * worker on the cell, the return value is 'null'
     *
     * @return the worker on the cell, if there is one (otherwise returns 'null')
     */
    public Worker getWorker() {
        return worker;
    }

    /**
     * The method is a simple getter of the level on the considered cell. As described in
     * "BlockType" class, there are five types of different levels: ground, first, second,
     * third and dome
     *
     * @return level of the cell
     */
    //tested
    public BlockType getLevel() {
        return level;
    }

    /**
     * The method is thought to perform a build on the considered cell. Every build
     * increases the level of the cell of one block, if it is possible. An exception is
     * thrown when the level is the highest one (dome), so no increase is possible
     *
     * @throws Exception when someone is trying to increase the level of the cell, but the
     * actual level is the highest one: a dome
     */
    //tested
    public void increaseLevel() throws Exception {
        if(this.level==BlockType.DOME) throw new Exception();
        else if (this.level==BlockType.LEVEL_THREE) {
            this.level=BlockType.DOME;
        }
        else if (this.level==BlockType.LEVEL_TWO) {
            this.level=BlockType.LEVEL_THREE;
        }
        else if (this.level==BlockType.LEVEL_ONE) {
            this.level=BlockType.LEVEL_TWO;
        }
        else {
            this.level=BlockType.LEVEL_ONE;
        }
    }

    /**
     * The method returns the difference between the level of the caller-cell and the
     * level of the parameter-cell, using attribute 'levelNumber' of class 'BlockType'.
     * So, if (for example) the considered cell is level two and the parameter is on the ground,
     * return value will be 2; instead, if the caller is level two and the parameter is
     * level three, return value will be -1.
     * Note that a movement from caller-cell to parameter-cell is possible only if the two
     * cells are adjacent, this method returns a value >=(-1) and the parameter level is
     * not a dome
     *
     * @param cell  cell you want to know level difference with
     * @return level difference between caller-cell and parameter-cell
     */
    //tested
    public int levelDifference(Cell cell) {
        return this.getLevel().getLevelNumber()-cell.getLevel().getLevelNumber();
    }

}
