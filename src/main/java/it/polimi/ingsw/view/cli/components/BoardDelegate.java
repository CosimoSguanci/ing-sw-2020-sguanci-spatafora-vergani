package it.polimi.ingsw.view.cli.components;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.PrintableColor;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.view.cli.Cli;

/**
 * BoardDelegate is a class in which board activities are managed.
 * Its constructor method allow BoardDelegate to have a reference to
 * a singular Client and the purpose of the class is summed up in
 * printBoard method.
 *
 * @author Andrea Vergani
 * @author Roberto Spatafora
 */
public class BoardDelegate {

    private final Cli cli;

    /**
     * This is the constructor of this class. At the moment of the creation
     * of a single instance of BoardDelegate the cli associated to it is set
     *
     * @param cli contains reference to the Cli associated
     */
    public BoardDelegate(Cli cli) {
        this.cli = cli;
    }

    /**
     * This private method bridges with BlockType enumeration.
     * With this method a correspondence between BlockType level and
     * a char that indicates it is made.
     *
     * @param level indicates the level of which you want the letter
     * @return a String, generally a single character, due to the implementation of the
     * BlockType.convertBlockTypeToUnicode() method, which corresponds to the level
     */
    private static String convertBlockTypeToUnicode(BlockType level) {
        return BlockType.convertBlockTypeToText(level);
    }

    /**
     * This method contains an algorithm to print the board game Cli version.
     * Every cell is printed as a 5x5; there are boarders which delimit each cell.
     *
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

        cli.println("");
        cli.println("");
        cli.println("");

        System.out.println("\t╔═══════════╦═══════════╦═══════════╦═══════════╦═══════════╗");
        System.out.println("\t║         " + convertBlockTypeToUnicode(gameBoard.getCell(0, 0).getLevel()) + " " +
                "║         " + convertBlockTypeToUnicode(gameBoard.getCell(0, 1).getLevel()) + " " +
                "║         " + convertBlockTypeToUnicode(gameBoard.getCell(0, 2).getLevel()) + " " +
                "║         " + convertBlockTypeToUnicode(gameBoard.getCell(0, 3).getLevel()) + " " +
                "║         " + convertBlockTypeToUnicode(gameBoard.getCell(0, 4).getLevel()) + " ║");

        System.out.print("\t");
        for (int j = 0; j < 5; j++) {
            System.out.print("║    ");
            if (!gameBoard.getCell(0, j).isEmpty()) {

                Worker printableWorker = gameBoard.getCell(0, j).getWorker();
                if (printableWorker.workerType.equals(Command.WORKER_FIRST)) {
                    System.out.print(Cli.convertColorToAnsi(printableWorker.player.getColor()) + " W1" + PrintableColor.RESET);
                } else {
                    System.out.print(Cli.convertColorToAnsi(printableWorker.player.getColor()) + " W2" + PrintableColor.RESET);
                }
            } else {
                System.out.print("   ");
            }
            System.out.print("    ");

        }


        System.out.print("║");
        System.out.println("\t" + rowIdentifier);
        rowIdentifier++;
        System.out.println("\t║           ║           ║           ║           ║           ║");
        for (int i = 1; i < 4; i++) {    //Single cell printed as 5x5: +---+ boarders; " "/"1"/"2" if worker is inside; BlockType specified.
            System.out.println("\t╠═══════════╬═══════════╬═══════════╬═══════════╬═══════════╣");
            System.out.println("\t║         " + convertBlockTypeToUnicode(gameBoard.getCell(i, 0).getLevel()) + " " +
                    "║         " + convertBlockTypeToUnicode(gameBoard.getCell(i, 1).getLevel()) + " " +
                    "║         " + convertBlockTypeToUnicode(gameBoard.getCell(i, 2).getLevel()) + " " +
                    "║         " + convertBlockTypeToUnicode(gameBoard.getCell(i, 3).getLevel()) + " " +
                    "║         " + convertBlockTypeToUnicode(gameBoard.getCell(i, 4).getLevel()) + " ║");

            System.out.print("\t");
            for (int j = 0; j < 5; j++) {
                System.out.print("║    ");
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
                System.out.print("    ");

            }


            System.out.print("║");
            System.out.println("\t" + rowIdentifier);
            rowIdentifier++;
            System.out.println("\t║           ║           ║           ║           ║           ║");
            //System.out.println("\t╚═══════════╩═══════════╩═══════════╩═══════════╩═══════════╝");

        }
        System.out.println("\t╠═══════════╬═══════════╬═══════════╬═══════════╬═══════════╣");
        System.out.println("\t║         " + convertBlockTypeToUnicode(gameBoard.getCell(4, 0).getLevel()) + " " +
                "║         " + convertBlockTypeToUnicode(gameBoard.getCell(4, 1).getLevel()) + " " +
                "║         " + convertBlockTypeToUnicode(gameBoard.getCell(4, 2).getLevel()) + " " +
                "║         " + convertBlockTypeToUnicode(gameBoard.getCell(4, 3).getLevel()) + " " +
                "║         " + convertBlockTypeToUnicode(gameBoard.getCell(4, 4).getLevel()) + " ║");

        System.out.print("\t");
        for (int j = 0; j < 5; j++) {
            System.out.print("║    ");
            if (!gameBoard.getCell(4, j).isEmpty()) {

                Worker printableWorker = gameBoard.getCell(4, j).getWorker();
                if (printableWorker.workerType.equals(Command.WORKER_FIRST)) {
                    System.out.print(Cli.convertColorToAnsi(printableWorker.player.getColor()) + " W1" + PrintableColor.RESET);
                } else {
                    System.out.print(Cli.convertColorToAnsi(printableWorker.player.getColor()) + " W2" + PrintableColor.RESET);
                }
            } else {
                System.out.print("   ");
            }
            System.out.print("    ");

        }


        System.out.print("║");
        System.out.println("\t" + rowIdentifier);
        System.out.println("\t║           ║           ║           ║           ║           ║");
        System.out.println("\t╚═══════════╩═══════════╩═══════════╩═══════════╩═══════════╝");


        System.out.println("\t      1           2           3           4           5    ");

        cli.println("");
        cli.println("");
        cli.println("");
    }
}
