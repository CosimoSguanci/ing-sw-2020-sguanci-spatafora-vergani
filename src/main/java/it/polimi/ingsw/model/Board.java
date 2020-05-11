package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.exceptions.InvalidCellException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * Board is the class that keeps information about the "Island Board" (mentioned in game
 * rules). It consists of 25 cells, in a 5x5 matrix configuration.
 * The class has also some methods to control some overview aspects of the game
 *
 * @author Andrea Mario Vergani
 * @author Cosimo Sguanci
 */
public class Board {
    public final static int WIDTH_SIZE = 5;
    public final static int HEIGHT_SIZE = 5;
   // private static final ConcurrentMap<String, Board> boardInstances = new ConcurrentHashMap<>();
    private Cell[][] board;
  //  private final String id;

    /**
     * Implements Multiton Pattern: one instance for each thread representing a match that's being played.
     *
     * @param key Thread id
     * @return a new instance of Board if the key was not already contained in boardInstances, otherwise the previous created instance.
     */
    /*public static Board getInstance(final String key) {
        return boardInstances.computeIfAbsent(key, Board::new);
    }

    public static void clearInstances() {
        boardInstances.clear();
    } */

    /**
     * The constructor creates the game board: in Santorini, it is a 5x5 space where
     * workers can move and build. Every cell of the board is initialized with its
     * constructor, so the whole board will be at ground level and without workers before
     * starting the match
     */
     Board() {
        //this.id = id;

        board = new Cell[WIDTH_SIZE][HEIGHT_SIZE];
        //initialization of a "empty" board at ground level (every cell with these starting configurations)
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Cell(i, j);
            }  //initialization of every cell in the board
        }

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
     */
    private boolean movementPossibleFromCell(Cell cell) {

        int row = cell.getRowIdentifier();  //i value for (i,j) coordinates of parameter
        int column = cell.getColIdentifier();  //j value for (i,j) coordinates of parameter
        for (int i = Math.max(row - 1, 0); i <= Math.min(row + 1, board.length - 1); i++) {
            for (int j = Math.max(column - 1, 0); j <= Math.min(column + 1, board[i].length - 1); j++) {
                if (i != row && j != column) {  //explore all adjacent cells
                    if (cell.isLevelDifferenceOk(board[i][j]) && board[i][j].getLevel() != BlockType.DOME && board[i][j].isEmpty()) {
                        return true;  //movement is possible if an adjacent cell is at lower, same or (+1) level, it is empty and not a dome
                    }
                }
            }
        }
        return false;
    }


    /**
     * The method controls if a movement is possible for a considered player, with one
     * of his workers
     *
     * @param player is the considered player, so the one you want to study possible movements
     * @return true if a movement in any direction by one of the workers of parameter-player is possible; otherwise, false
     */
    public boolean canMove(Player player) {
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
     */
    public boolean canBuild(Worker worker) {

        /*int row = worker.getPosition().getRowIdentifier();  //i value for (i,j) coordinates of worker's cell
        int column = worker.getPosition().getColIdentifier();  //j value for (i,j) coordinates of worker's cell*/

        /*for (int i = Math.max(row - 1, 0); i <= Math.min(row + 1, board.length - 1); i++) {
            for (int j = Math.max(column - 1, 0); j <= Math.min(column + 1, board[i].length - 1); j++) {
                    if (board[i][j].getLevel() != BlockType.DOME && board[i][j].isEmpty()) {
                        return true;  //building is possible if an adjacent cell is not a dome and it is empty (there is no worker on it)
                    }
            }
        }*/


        for (int i = 0; i < Board.WIDTH_SIZE; i++) {
            for(int j = 0; j < Board.HEIGHT_SIZE; j++) {
                if(board[i][j].isAdjacentTo(worker.getPosition()) && board[i][j].getLevel() != BlockType.DOME && board[i][j].isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * The method controls if it is possible to return a reference to a Cell of the board
     *
     * @param row indicates the row of the Cell requested in the board
     * @param col indicates the column of the Cell requested in the board
     * @return a reference to the Cell requested if possible
     * @throws InvalidCellException if the parameters are not valid for a Cell in the board.
     */
    public Cell getCell(int row, int col) throws InvalidCellException {
        if ((row >= 0) && (row < board.length) && (col >= 0) && (col < board[0].length)) {
            return board[row][col];
        } else throw new InvalidCellException();
    }

    @Override
    public String toString() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(this);
    }
}