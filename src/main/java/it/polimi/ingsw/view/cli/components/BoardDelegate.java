package it.polimi.ingsw.view.cli.components;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.view.cli.Cli;

public class BoardDelegate {

    private final Cli cli;

    public BoardDelegate(Cli cli) {
        this.cli = cli;
    }

    /**
     * This method contains an algorithm to print the board game Cli version.
     * Every cell is printed as a 5x5; there are boarders which delimit each cell.
     * @param board indicates the board which is used in the relative match.
     *              It contains references to each cell (including their level
     *              and workers if there are on that cell).
     */
    public void printBoard(String board) {
        cli.setCurrentBoard(board);  //new Gson version of board is saved

        GsonBuilder builder = new GsonBuilder();

        Gson gson = builder.create();
        Board gameBoard = gson.fromJson(board, Board.class);


        char rowIdentifier = 'A';

        cli.print("");
        cli.print("");
        cli.print("");

        for (int i = 0; i < 5; i++) {    //Single cell printed as 5x5: +---+ boarders; " "/"1"/"2" if worker is inside; BlockType specified.
            System.out.println("\t+  -  -  -  +  +  -  -  -  +  +  -  -  -  +  +  -  -  -  +  +  -  -  -  +");
            System.out.println("\t|         " + convertBlockTypeToUnicode(gameBoard.getCell(i, 0).getLevel()) + " | " +
                    " |         " + convertBlockTypeToUnicode(gameBoard.getCell(i, 1).getLevel()) + " | " +
                    " |         " + convertBlockTypeToUnicode(gameBoard.getCell(i, 2).getLevel()) + " | " +
                    " |         " + convertBlockTypeToUnicode(gameBoard.getCell(i, 3).getLevel()) + " | " +
                    " |         " + convertBlockTypeToUnicode(gameBoard.getCell(i, 4).getLevel()) + " | ");

            System.out.print(rowIdentifier + "\t");
            rowIdentifier++;

            for (int j = 0; j < 5; j++) {
                System.out.print("|    ");
                if (!gameBoard.getCell(i, j).isEmpty()) {

                    Worker printableWorker = gameBoard.getCell(i, j).getWorker();
                    if (printableWorker.workerType.equals(Command.WORKER_FIRST)) {
                        System.out.print(Cli.convertColorToAnsi(printableWorker.player.getColor()) + " W1" + PrintableColor.RESET);
                    } else {
                        System.out.print(Cli.convertColorToAnsi(printableWorker.player.getColor()) + " W2" + PrintableColor.RESET);
                    }
                } else {
                    System.out.print("   ");
                }
                System.out.print("    |  ");

            }

            System.out.println();
            System.out.println("\t|           |  |           |  |           |  |           |  |           |");
            System.out.println("\t+  -  -  -  +  +  -  -  -  +  +  -  -  -  +  +  -  -  -  +  +  -  -  -  +");

        }


        System.out.println("\t      1              2              3              4              5    ");


        cli.print("");
        cli.print("");
        cli.print("");
    }

    private //TODO eliminate: getLevelNumber() is a BlockType method.
    static String convertBlockTypeToUnicode(BlockType level) { // todo move to BlockType
        switch (level) {
            case GROUND:
                return "0";
            case LEVEL_ONE:
                return "1";
            case LEVEL_TWO:
                return "2";
            case LEVEL_THREE:
                return "3";
            case DOME:
                return "D";
            default:
                throw new IllegalArgumentException();
        }
    }
}
