package it.polimi.ingsw.model;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


public class BoardTest {

    @Disabled
    @Test
    public void hasWinnerWorkerTest() {
    }


    @Test
    public void canMoveTrueTest() throws Exception {
        int playersNum = 2;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Board board = Board.getInstance(key);;

        Player player = new Player("Andrea", "andv", match);
        Worker w1 = new Worker(player, board);
        Worker w2 = new Worker(player, board);
        player.setWorkerFirst(w1);
        player.setWorkerSecond(w2);

        Player opponent = new Player("Marco", "mc", match);
        Worker wOpp1 = new Worker(opponent, board);
        Worker wOpp2 = new Worker(opponent, board);
        opponent.setWorkerFirst(wOpp1);
        opponent.setWorkerSecond(wOpp2);

        board.getCell(4,1).setLevel(BlockType.LEVEL_TWO);

        try {
            w1.setInitialPosition(2, 3);
            w2.setInitialPosition(4, 1);
            wOpp1.setInitialPosition(1, 3);
            wOpp2.setInitialPosition(3, 3);
        } catch(Exception e) {
            e.printStackTrace();
        }

        board.getCell(1,2).setLevel(BlockType.DOME);
        board.getCell(1,4).setLevel(BlockType.DOME);
        board.getCell(2,2).setLevel(BlockType.LEVEL_TWO);
        board.getCell(2,4).setLevel(BlockType.LEVEL_THREE);
        board.getCell(3,0).setLevel(BlockType.DOME);
        board.getCell(3,1).setLevel(BlockType.DOME);
        board.getCell(3,2).setLevel(BlockType.LEVEL_TWO);
        board.getCell(3,4).setLevel(BlockType.LEVEL_TWO);
        board.getCell(4,0).setLevel(BlockType.DOME);
        board.getCell(4,2).setLevel(BlockType.DOME);

        assertTrue(board.canMove(player));
        assertTrue(board.canMove(opponent));
    }


    @Test
    public void canMoveFalseTest() throws Exception {
        int playersNum = 2;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Board board = Board.getInstance(key);;
        Player player = new Player("Andrea", "andv", match);
        Worker w1 = new Worker(player, board);
        Worker w2 = new Worker(player, board);
        player.setWorkerFirst(w1);
        player.setWorkerSecond(w2);

        Player opponent = new Player("Marco", "mc", match);
        Worker wOpp1 = new Worker(opponent, board);
        Worker wOpp2 = new Worker(opponent, board);
        opponent.setWorkerFirst(wOpp1);
        opponent.setWorkerSecond(wOpp2);

        try {
            w1.setInitialPosition(2, 3);
            w2.setInitialPosition(4, 1);
            wOpp1.setInitialPosition(1, 3);
            wOpp2.setInitialPosition(3, 3);
        } catch(Exception e) {
            e.printStackTrace();
        }


        board.getCell(1,2).setLevel(BlockType.DOME);
        board.getCell(1,4).setLevel(BlockType.DOME);
        board.getCell(2,2).setLevel(BlockType.LEVEL_TWO);
        board.getCell(2,4).setLevel(BlockType.LEVEL_THREE);
        board.getCell(3,0).setLevel(BlockType.DOME);
        board.getCell(3,1).setLevel(BlockType.DOME);
        board.getCell(3,2).setLevel(BlockType.LEVEL_TWO);
        board.getCell(3,4).setLevel(BlockType.LEVEL_TWO);
        board.getCell(4,0).setLevel(BlockType.DOME);
        board.getCell(4,2).setLevel(BlockType.DOME);

        assertFalse(board.canMove(player));
        assertTrue(board.canMove(opponent));
    }


    @Test
    public void canBuildTrueTest() throws Exception {
        int playersNum = 2;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Board board = Board.getInstance(key);;
        Player player = new Player("Andrea", "andv", match);
        Worker w1 = new Worker(player, board);
        Worker w2 = new Worker(player, board);
        player.setWorkerFirst(w1);
        player.setWorkerSecond(w2);

        Player opponent = new Player("Marco", "mc",match);
        Worker wOpp1 = new Worker(opponent, board);
        Worker wOpp2 = new Worker(opponent, board);
        opponent.setWorkerFirst(wOpp1);
        opponent.setWorkerSecond(wOpp2);

        board.getCell(4,1).setLevel(BlockType.LEVEL_TWO);

        try {
            w1.setInitialPosition(2, 3);
            w2.setInitialPosition(4, 1);
            wOpp1.setInitialPosition(1, 3);
            wOpp2.setInitialPosition(3, 3);
        } catch(Exception e) {
            e.printStackTrace();
        }

        board.getCell(1,2).setLevel(BlockType.DOME);
        board.getCell(1,4).setLevel(BlockType.DOME);
        board.getCell(2,2).setLevel(BlockType.LEVEL_TWO);
        board.getCell(2,4).setLevel(BlockType.LEVEL_THREE);
        board.getCell(3,0).setLevel(BlockType.DOME);
        board.getCell(3,1).setLevel(BlockType.DOME);
        board.getCell(3,2).setLevel(BlockType.LEVEL_TWO);
        board.getCell(3,4).setLevel(BlockType.LEVEL_TWO);
        board.getCell(4,0).setLevel(BlockType.DOME);
        board.getCell(4,2).setLevel(BlockType.DOME);

        assertTrue(board.canBuild(w1));
        assertTrue(board.canBuild(w2));
        assertTrue(board.canBuild(wOpp1));
        assertTrue(board.canBuild(wOpp2));
    }


    @Test
    public void canBuildFalseTest() throws Exception {
        int playersNum = 2;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Board board = Board.getInstance(key);;
        Player player = new Player("Andrea", "andv", match);
        Worker w1 = new Worker(player, board);
        Worker w2 = new Worker(player, board);
        player.setWorkerFirst(w1);
        player.setWorkerSecond(w2);

        try {
            w1.setInitialPosition(0, 4);
            w2.setInitialPosition(1, 4);
        } catch(Exception e) {
            e.printStackTrace();
        }

        board.getCell(0,3).setLevel(BlockType.DOME);
        board.getCell(1,3).setLevel(BlockType.DOME);
        board.getCell(2,3).setLevel(BlockType.LEVEL_THREE);
        board.getCell(2,4).setLevel(BlockType.DOME);

        assertFalse(board.canBuild(w1));
        assertTrue(board.canBuild(w2));
    }

    @Disabled
    @Test
    public void getCell() {
    }
}