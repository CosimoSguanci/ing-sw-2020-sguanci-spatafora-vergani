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




        /*Match match = Match.getInstance("AAA", 2);
        Board gameBoard = match.getMatchBoard();
        Player player1 = new Player("RobS", match);
        Player player2 = new Player("CosS", match);

        player1.setColor(PrintableColour.PURPLE);
        player2.setColor(PrintableColour.YELLOW);

        player1.getWorkerFirst().setInitialPosition(0,0);
        player1.getWorkerSecond().setInitialPosition(1,0);
        player2.getWorkerFirst().setInitialPosition(0,1);
        player2.getWorkerSecond().setInitialPosition(1,1);

        char rowIdentifier = 'A';

        for (int i = 0; i < 5; i++) {    //Single cell printed as 5x5: +---+ board; " "/"1"/"2" if worker is inside; BlockType specified.
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
                    if (printableWorker.equals(printableWorker.player.getWorkerFirst())) {
                        System.out.print(convertColorToAnsi(printableWorker.player.getColor()) + "\u265F 1" + PrintableColour.RESET);
                    } else {
                        System.out.print(convertColorToAnsi(printableWorker.player.getColor()) + "\u265F 2" + PrintableColour.RESET);
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

        System.out.println("\t    1         2         3         4         5    ");*/






    }

    private static String convertBlockTypeToUnicode(BlockType level) {
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

    private static String convertColorToAnsi(PrintableColour color) {
        switch (color) {
            case RED:
                return "\u001B[31m";
            case BLUE:
                return "\u001B[34m";
            case GREEN:
                return "\u001B[32m";
            case YELLOW:
                return "\u001B[33m";
            case PURPLE:
                return "\u001B[35m";
            default:
                throw new IllegalArgumentException();
        }
        

    }
}