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
    private final int rowIdentifier;
    private final int colIdentifier;


    /**
     * Cell is the builder of the class. When a cell is created, its level is the ground
     * level and no worker is on it
     */
    public Cell(int rowIdentifier, int colIdentifier) {
        this.level = BlockType.GROUND;
        this.worker = null;
        this.rowIdentifier = rowIdentifier;
        this.colIdentifier = colIdentifier;
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
     * The method is a simple setter of the worker on the considered cell.
     */
    public void setWorker(Worker worker) {
         this.worker = worker;
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
    public void increaseLevel() {

        switch(this.level) {
            case GROUND:
                this.level = BlockType.LEVEL_ONE;
                break;
            case LEVEL_ONE:
                this.level = BlockType.LEVEL_TWO;
                break;
            case LEVEL_TWO:
                this.level = BlockType.LEVEL_THREE;
                break;
            case LEVEL_THREE:
                this.level = BlockType.DOME;
                break;
            /*case DOME:
                throw new Exception();*/

        }
    }


    //may be useful for Gods that can build a dome everywhere
    public void setLevel(BlockType level) {
        this.level = level;
    }


    /**
     * The method returns the difference between the level of the caller-cell and the
     * level of the parameter-cell, using attribute 'levelNumber' of class 'BlockType'.
     * So, if (for example) the considered cell is level two and the parameter is on the ground,
     * return value will be 2; instead, if the caller is level two and the parameter is
     * level three, return value will be -1.
     * Note that a movement from caller-cell to parameter-cell is possible only if the two
     * cells are adjacent, this method returns a value >=(-1), the parameter level is
     * not a dome and there is no worker on it
     *
     * @param cell  cell you want to know level difference with
     * @return level difference between caller-cell and parameter-cell
     */
    //tested
    public int levelDifference(Cell cell) {
        return this.getLevel().getLevelNumber()-cell.getLevel().getLevelNumber();
    }


    /**
     * The method returns a boolean that represents the possibility to move from
     * caller-cell to parameter-cell, based on level difference between the two
     * cells. Considering only level difference, this method returns true (so the
     * movement is possible) if parameter-cell is at most one level higher than
     * caller-cell.
     * Note that a movement from caller-cell to parameter-cell is possible only if the two
     * cells are adjacent, this method returns true, the parameter level is
     * not a dome and there is no worker on it
     *
     * @param destination  cell you want to move to
     * @return true if level difference between caller-cell and parameter-cell is >=(-1)
     */
    public boolean isLevelDifferenceOk (Cell destination) {
        return this.levelDifference(destination) >= -1;
    }

    /**
     * The method is a getter of the row index of the relative cell in the board.
     *
     * @return the int number that identifies the cell in board's rows.*/
    public int getRowIdentifier() {
        return this.rowIdentifier;
    }


    /**
     * The method is a getter of the column index of the relative cell in the board.
     *
     * @return the int number that identifies the cell in board's columns.*/
    public int getColIdentifier() {
        return this.colIdentifier;
    }



    /**
     * The method controls if the caller-cell is adjacent to the parameter-cell in
     * the board. Reading game rules, every cell has (up to) eight neighbouring
     * spaces: so, two cells are adjacent when they have at least one point in common
     *
     * @param other   possible adjacent cell to the caller-cell
     * @return true if parameter-cell and caller-cell are adjacent; otherwise, false
     *
     */
    public boolean isAdjacentTo(Cell other) {
        int iDiff = this.getRowIdentifier()-other.getRowIdentifier();  //iDiff contains difference between i values of (i,j) coordinates for the two cells
        int jDiff = this.getColIdentifier()-other.getColIdentifier();  //jDiff contains difference between j values of (i,j) coordinates for the two cells
        //case adjacent cells: absolute value of difference between cells' coordinates is <=1, so a cell "surrounds" the other one
        //in other cases, cells are not adjacent
        if(iDiff == 0 && jDiff == 0) return false;  //case 'first' and 'second' are the same cell, so they are not adjacent
        else return (Math.abs(iDiff) <= 1 && Math.abs(jDiff) <= 1);
    }

}
