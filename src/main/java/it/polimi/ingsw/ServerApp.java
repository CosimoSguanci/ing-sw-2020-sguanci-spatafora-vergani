package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.server.Server;

public class ServerApp {
    public static void main(String[] args) {
        try {
            Server server = new Server();
            Server.setIsActive(true);
            server.run();
        } catch (Exception e) {
            System.err.println("Uncaught Exception: " + e.getMessage());
            Server.setIsActive(false);
            System.exit(0);
        }

        /*


        Match match = Match.getInstance("AAA", 2);
        Board gameBoard = match.getMatchBoard();
        Player player1 = new Player("RobS", match);
        Player player2 = new Player("CosS", match);

        player1.setColor(PrintableColour.GREEN);
        player2.setColor(PrintableColour.BLUE);

        player1.getWorkerFirst().setInitialPosition(1,0);
        player1.getWorkerSecond().setInitialPosition(2,3);
        player2.getWorkerFirst().setInitialPosition(2,0);
        player2.getWorkerSecond().setInitialPosition(2,2);

        char rowIdentifier = 'A';

        System.out.println("\t      1              2              3              4              5    ");
        for (int i = 0; i < 5; i++) {    //Single cell printed as 5x5: +---+ board; " "/"1"/"2" if worker is inside; BlockType specified.
            System.out.println("\t+  -  -  -  +  +  -  -  -  +  +  -  -  -  +  +  -  -  -  +  +  -  -  -  +");
            System.out.println("\t|        " + convertBlockTypeToUnicode(gameBoard.getCell(i, 0).getLevel()) + "  | "+
                    " |        " + convertBlockTypeToUnicode(gameBoard.getCell(i, 1).getLevel()) + "  | " +
                    " |        " + convertBlockTypeToUnicode(gameBoard.getCell(i, 2).getLevel()) + "  | " +
                    " |        " + convertBlockTypeToUnicode(gameBoard.getCell(i, 3).getLevel()) + "  | " +
                    " |        " + convertBlockTypeToUnicode(gameBoard.getCell(i, 4).getLevel()) + "  |");

            System.out.print(rowIdentifier + "\t");
            rowIdentifier++;

            for (int j = 0; j < 5; j++) {
                System.out.print("|    ");
                if (!gameBoard.getCell(i, j).isEmpty()) {
                    Worker printableWorker = gameBoard.getCell(i, j).getWorker();
                    if (printableWorker.equals(printableWorker.player.getWorkerFirst())) {
                        System.out.print(convertColorToAnsi(printableWorker.player.getColor()) + " W1" + PrintableColour.RESET);
                    } else {
                        System.out.print(convertColorToAnsi(printableWorker.player.getColor()) + " W2" + PrintableColour.RESET);
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


    }

    private static String convertBlockTypeToUnicode(BlockType level) {
        switch (level) {
            case GROUND:
                return "\u2070";
            case LEVEL_ONE:
                return "\u00B9";
            case LEVEL_TWO:
                return "\u00B2";
            case LEVEL_THREE:
                return "\u00B3";
            case DOME:
                return "\u2074";
            default:
                throw new IllegalArgumentException();
        }
    }

    private static String convertColorToAnsi(PrintableColour color) {
        switch (color) {
            case RED:
                return "\u001B[31m";
            case BLUE:
                return "\u001B[34m";
            case GREEN:
                return "\u001B[32m";
            case YELLOW:
                return "\u001B[43m";
            case PURPLE:
                return "\u001B[35m";
            default:
                throw new IllegalArgumentException();
        }
        
         */
    }
}