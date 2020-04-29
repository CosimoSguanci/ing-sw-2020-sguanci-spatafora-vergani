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
        Match match = Match.getInstance("aaa", 2);
        Board gameBoard = match.getMatchBoard();
        Player player1 = new Player("ID1", "RobS", match);
        Player player2 = new Player("ID2", "CosS", match);
        player1.setColor(PrintableColour.GREEN);
        player2.setColor(PrintableColour.RED);

        player1.getWorkerFirst().setInitialPosition(0,0);
        player1.getWorkerSecond().setInitialPosition(3,2);
        player2.getWorkerFirst().setInitialPosition(1,0);
        player2.getWorkerSecond().setInitialPosition(4,4);

        gameBoard.getCell(0,1).setLevel(BlockType.LEVEL_TWO);
        gameBoard.getCell(3,1).setLevel(BlockType.LEVEL_TWO);
        gameBoard.getCell(4,2).setLevel(BlockType.LEVEL_ONE);
        gameBoard.getCell(1,4).setLevel(BlockType.LEVEL_THREE);
        gameBoard.getCell(2,4).setLevel(BlockType.DOME);


        for(int i = 0; i < 5; i++) {    //Single cell printed as 5x5: +---+ board; " "/"1"/"2" if worker is inside; BlockType specified.
            System.out.println("+ - - - + + - - - + + - - - + + - - - + + - - - +");
            System.out.println("|     " + convertBlockTypeToUnicode(gameBoard.getCell(i, 0).getLevel()) + " | " +
                    "|     " + convertBlockTypeToUnicode(gameBoard.getCell(i, 1).getLevel()) + " | " +
                    "|     " + convertBlockTypeToUnicode(gameBoard.getCell(i, 2).getLevel()) + " | " +
                    "|     " + convertBlockTypeToUnicode(gameBoard.getCell(i, 3).getLevel()) + " | " +
                    "|     " + convertBlockTypeToUnicode(gameBoard.getCell(i, 4).getLevel()) + " |");

            for(int j = 0; j < 5; j++) {
                System.out.print("|   " );
                if(!gameBoard.getCell(i, j).isEmpty()) {
                    Worker printableWorker = gameBoard.getCell(i, j).getWorker();
                    if(printableWorker.equals(printableWorker.player.getWorkerFirst())) {
                        System.out.print(convertColorToAnsi(printableWorker.player.getColor()) + "\uD83C\uDFC3\u2081\t" + PrintableColour.RESET);
                    } else {System.out.print(convertColorToAnsi(printableWorker.player.getColor()) + "\uD83C\uDFC3\u2082\t" + PrintableColour.RESET);}
                }
                else {System.out.print("    ");}
                System.out.print("| " );
            }

            System.out.println();
            System.out.println("|       | |       | |       | |       | |       |");
            System.out.println("+ - - - + + - - - + + - - - + + - - - + + - - - +");

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
            case GREEN:
                return "\u001B[32m";
            case BLUE:
                return "\u001B[34m";
            default:
                throw new IllegalArgumentException();
        }
*/
    }
}
