package it.polimi.ingsw.model;

import java.util.ArrayList;

/**
 * Board is the class that keeps information about the "Island Board" (mentioned in game
 * rules). It consists of 25 cells, in a 5x5 matrix configuration.
 * The class has also some methods to control some overview aspects of the game
 *
 * @author Andrea Mario Vergani
 */
public class Board {
    private Cell[][] board;

    /**
     * The constructor creates the game board: in Santorini, it is a 5x5 space where
     * workers can move and build. Every cell of the board is initialized with its
     * constructor, so the whole board will be at ground level and without workers before
     * starting the match
     */
    public Board() {
        board = new Cell[5][5];
        //initialization of a "empty" board at ground level (every cell with these starting configurations)
        for(int i=0; i<board.length; i++) {
            for(int j=0; j<board[i].length; j++) {
                board[i][j] = new Cell();
            }  //initialization of every cell in the board
        }
    }

    /**
     * This private method helps in getting the coordinates (i,j) of a cell in the board.
     * Knowing coordinates is, in fact, very useful in other (public) methods of this class:
     * analyze possible movements, buildings and other things can be done only thanks to
     * "localization" of cells in the board, since almost all the operations in Santorini
     * involve adjacent cells
     *
     * @param cell  the considered cell, so the one you want to know coordinates
     * @return (i,j) coordinates (in 5x5 board) of parameter-cell, in the form of ArrayList<Integer>
     * @throws Exception    when cell is not in the board, so something went wrong
     *
     */
    private ArrayList<Integer> getCoordinates (Cell cell) throws Exception {
        ArrayList<Integer> coordinates = new ArrayList<>();

        for(int i=0; i<board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {  //consider every cell separately
                if (board[i][j] == cell) {
                    coordinates.add(0, i);
                    coordinates.add(1, j);
                }  //coordinates now contains (row,column) indication for 'cell' parameter
            }
        }  //end external cycle

        if(coordinates.size()!=2) throw new Exception();
        return coordinates;
    }


    /**
     * The method controls if two cells in the board are adjacent. Reading game rules,
     * every cell has (up to) eight neighbouring spaces: so, two cells are adjacent
     * when they have at least one point in common
     *
     * @param first  one of the considered cells
     * @param second  the other cell
     * @return true if the parameters are adjacent; otherwhise, false
     * @throws Exception    if a method called inside throws Exception
     *
     */
    public boolean adjacent(Cell first, Cell second) throws Exception {
        ArrayList<Integer> firstCoord = getCoordinates(first);
        ArrayList<Integer> secondCoord = getCoordinates(second);
        int iDiff = ((firstCoord.get(0))-(secondCoord.get(0)));  //iDiff contains difference between i values of (i,j) coordinates for 'first' and 'second' parameters
        int jDiff = (firstCoord.get(1))-(secondCoord.get(1));  //jDiff contains difference between j values of (i,j) coordinates for 'first' and 'second' parameters
        //case adjacent cells: absolute value of difference between cells' coordinates is <=1, so a cell "surrounds" the other one
        //in other cases, cells are not adjacent
        if(iDiff == 0 && jDiff == 0) return false;  //case 'first' and 'second' are the same cell, so they are not adjacent
        else return (Math.abs(iDiff) <= 1 && Math.abs(jDiff) <= 1);
    }

    /**
     * The method controls if a player won the match, with one of his/her two workers.
     * Santorini rules explain that (in normal situations) a player wins when, during his
     * turn, one of his workers moves up on top of level three. This methods just controls
     * if one of the two workers of the parameter-player is on top of third level; so,
     * it is caller problem when to call this method, since here there is no control about
     * last move
     *
     * @param player is the considered player of the match
     * @return true if one of player's workers are on top of third level
     *
     */
    public boolean hasWinnerWorker(Player player) {
        Cell cellOne = player.getWorkerFirst().getPosition();  //actual cell of the first worker
        Cell cellTwo = player.getWorkerSecond().getPosition();  //actual cell of the second worker
        return cellOne.getLevel() == BlockType.LEVEL_THREE || cellTwo.getLevel() == BlockType.LEVEL_THREE;
    }

    /**
     * The method controls if a movement is possible from a considered cell. Santorini
     * rules explain that a movement from a cell to another one (in normal situations) is
     * possible only if these two are adjacent, the movement is maximum one level up and
     * the destination cell is not at dome level.
     * This method is private because it supports other methods of this class
     *
     * @param cell is the starting cell, from which possible movements must be considered
     * @return true if a movement in any direction from parameter-cell is possible; otherwise, false
     * @throws Exception    if a method called inside throws Exception
     *
     */
    private boolean movementPossibleFromCell(Cell cell) throws Exception {
        ArrayList<Integer> coord = getCoordinates(cell);
        int row = coord.get(0);  //i value for (i,j) coordinates of parameter
        int column = coord.get(1);  //j value for (i,j) coordinates of parameter
        for(int i = Math.max(row-1, 0); i <= Math.min(row+1, board.length-1); i++) {
            for(int j = Math.max(column-1, 0); j<= Math.min(column+1, board[i].length-1); j++) {
                if(i!=row && j!=column) {  //explore all adjacent cells
                    if (cell.levelDifference(board[i][j]) >= -1 && board[i][j].getLevel() != BlockType.DOME && board[i][j].isEmpty()) {
                        return true;  //movement is possible if an adjacent cell is at lower, same or (+1) level, it is empty and not a dome
                    }
                }
            }  //end internal cycle
        }  //end external cycle
        return false;
    }


    /**
     * The method controls if a movement is possible for a considered player, with one
     * of his workers
     *
     * @param player is the considered player, so the one you want to study possible movements
     * @return true if a movement in any direction by one of the workers of parameter-player is possible; otherwise, false
     * @throws Exception    if a method called inside throws Exception
     *
     */
    public boolean canMove(Player player) throws Exception {
        Cell cellOne = player.getWorkerFirst().getPosition();  //actual cell of the first worker
        Cell cellTwo = player.getWorkerSecond().getPosition();  //actual cell of the second worker
        boolean possibleOne = movementPossibleFromCell(cellOne);  //true if movement from cellOne is possible, so first worker can move somewhere
        boolean possibleTwo = movementPossibleFromCell(cellTwo);  //true if movement from cellTwo is possible, so second worker can move somewhere
        return (possibleOne || possibleTwo);
    }

    /**
     * The method controls if a building is possible for a considered worker. Santorini
     * rules explain that a worker in a cell can build (in normal situations) in an
     * adjacent cell, increasing its level; if the adjacent cell is at dome level, building
     * is not possible over it
     *
     * @param worker is the considered worker, so the one you want to study possible buildings
     * @return true if a building in any direction by parameter-worker is possible; otherwise, false
     *
     */
    public boolean canBuild(Worker worker) throws Exception {
        ArrayList<Integer> coord = getCoordinates(worker.getPosition());
        int row = coord.get(0);  //i value for (i,j) coordinates of parameter
        int column = coord.get(1);  //j value for (i,j) coordinates of parameter
        for(int i = Math.max(row-1, 0); i <= Math.min(row+1, board.length-1); i++) {
            for(int j = Math.max(column-1, 0); j<= Math.min(column+1, board[i].length-1); j++) {
                if(i!=row && j!=column) {  //explore all adjacent cells
                    if (board[i][j].getLevel() != BlockType.DOME && board[i][j].isEmpty()) {
                        return true;  //building is possible if an adjacent cell is not a dome and it is empty (there is no worker on it)
                    }
                }
            }  //end internal cycle
        }  //end external cycle
        return false;
    }

    /**
     * The method controls if it is possible to return a reference to a Cell of the board
     *
     * @param row indicates the row of the Cell requested in the board
     * @param col indicates the column of the Cell requested in the board
     *
     * @return a reference to the Cell requested if possible
     * @throws Exception if the parameters are not valid for a Cell in the board.
     * */
    public Cell getCell(int row, int col) throws Exception {
        if((row >= 0) && (row < board.length) && (col >= 0) && (col < board[0].length)) {
            return board[row][col];
        }
        throw new Exception(); // invalidCellRequestedException()
    }

}