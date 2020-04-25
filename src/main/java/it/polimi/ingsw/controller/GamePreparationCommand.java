package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.BadPlayerCommandException;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;

import java.io.Serializable;
import java.util.Arrays;

public class GamePreparationCommand implements Serializable {

    private String playerID;

    private transient Player player;
    private transient Cell workerFirstCell;
    private transient Cell workerSecondCell;

    public final int workerFirstRow;
    public final int workerFirstCol;

    public final int workerSecondRow;
    public final int workerSecondCol;

    public GamePreparationCommand(int workerFirstRow, int workerFirstCol, int workerSecondRow, int workerSecondCol) {

        this.workerFirstRow = workerFirstRow;
        this.workerFirstCol = workerFirstCol;

        this.workerSecondRow = workerSecondRow;
        this.workerSecondCol = workerSecondCol;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setWorkerFirstCell(Cell workerFirstCell) {
        this.workerFirstCell = workerFirstCell;
    }

    public void setWorkerSecondCell(Cell workerSecondCell) {
        this.workerSecondCell = workerSecondCell;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Cell getWorkerFirstCell() {
        return workerFirstCell;
    }

    public Cell getWorkerSecondCell() {
        return workerSecondCell;
    }

    public Player getPlayer() {
        return player;
    }

    public static GamePreparationCommand parseInput(String command) throws BadPlayerCommandException {
        // place w1 c1 w2 a2
        try {

            String[] s;

            String[] initialString = command.split("\\s+");

            if(initialString[0].length() == 0) {  //case command starting with space
                s = Arrays.copyOfRange(initialString, 1, initialString.length);
            }
            else{
                s = Arrays.copyOf(initialString, initialString.length);
            }

            if (s.length != 5) {
                throw new BadPlayerCommandException(); // TODO BadCommand
            }

            String type = s[0];


            if(!type.toLowerCase().equals("place")) {
                throw new BadPlayerCommandException(); // TODO BadCommand
            }


            String workerFirst = s[1];

            if (!workerFirst.toLowerCase().equals(PlayerCommand.WORKER_FIRST)) {
                throw new BadPlayerCommandException();
            }

            String cellWorkerFirst = s[2].toLowerCase();

            char rowCharW1 = cellWorkerFirst.charAt(0);
            int colNumW1 = Integer.parseInt(cellWorkerFirst.substring(1));

            if (cellWorkerFirst.length() != 2 || colNumW1 < 1 || colNumW1 > Board.WIDTH_SIZE || rowCharW1 < 'a' || rowCharW1 >= ('a' + Board.HEIGHT_SIZE)) {
                throw new BadPlayerCommandException();
            }

            String workerSecond = s[3];

            if (!workerSecond.toLowerCase().equals(PlayerCommand.WORKER_SECOND)) {
                throw new BadPlayerCommandException();
            }

            String cellWorkerSecond = s[4].toLowerCase();

            char rowCharW2 = cellWorkerSecond.charAt(0);
            int colNumW2 = Integer.parseInt(cellWorkerSecond.substring(1));

            if (cellWorkerSecond.length() != 2 || colNumW2 < 1 || colNumW2 > Board.WIDTH_SIZE || rowCharW2 < 'a' || rowCharW2 >= ('a' + Board.HEIGHT_SIZE)) {
                throw new BadPlayerCommandException();
            }

            if(rowCharW1 == rowCharW2 && colNumW1 == colNumW2) {
                throw new BadPlayerCommandException();
            }

            int rowNumW1 = (int) rowCharW1 - 'a';
            int rowNumW2 = (int) rowCharW2 - 'a'; // TODO col - 1

            return new GamePreparationCommand(rowNumW1, colNumW1, rowNumW2, colNumW2);
        }
        catch(Exception e) {
            throw new BadPlayerCommandException();
        }


    }
}
