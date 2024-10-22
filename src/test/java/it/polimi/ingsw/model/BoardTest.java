package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidCellException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class BoardTest {

    private Board board;
    private Player player;
    private Worker w1;
    private Worker w2;
    private Player opponent;
    private Worker wOpp1;
    private Worker wOpp2;

    @BeforeEach
    public void initBoardProperties() {
        int playersNum = 2;
        Match match = new Match(playersNum);
        this.board = match.getMatchBoard();

        this.player = new Player("Andrea", new Model(match), match);
        this.w1 = player.getWorkerFirst();
        this.w2 = player.getWorkerSecond();

        this.opponent = new Player("Marco", new Model(match), match);
        this.wOpp1 = opponent.getWorkerFirst();
        this.wOpp2 = opponent.getWorkerSecond();
    }


    @Test
    public void canMoveTrueTest() {

        board.getCell(4, 1).setLevel(BlockType.LEVEL_TWO);

        try {
            w1.setInitialPosition(2, 3);
            w2.setInitialPosition(4, 1);
            wOpp1.setInitialPosition(1, 3);
            wOpp2.setInitialPosition(3, 3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        board.getCell(1, 2).setLevel(BlockType.DOME);
        board.getCell(1, 4).setLevel(BlockType.DOME);
        board.getCell(2, 2).setLevel(BlockType.LEVEL_TWO);
        board.getCell(2, 4).setLevel(BlockType.LEVEL_THREE);
        board.getCell(3, 0).setLevel(BlockType.DOME);
        board.getCell(3, 1).setLevel(BlockType.DOME);
        board.getCell(3, 2).setLevel(BlockType.LEVEL_TWO);
        board.getCell(3, 4).setLevel(BlockType.LEVEL_TWO);
        board.getCell(4, 0).setLevel(BlockType.DOME);
        board.getCell(4, 2).setLevel(BlockType.DOME);

        assertTrue(board.canMove(player));
        assertTrue(board.canMove(opponent));
    }


    @Test
    public void canMoveFalseTest() {

        try {
            w1.setInitialPosition(2, 3);
            w2.setInitialPosition(4, 1);
            wOpp1.setInitialPosition(1, 3);
            wOpp2.setInitialPosition(3, 3);
        } catch (Exception e) {
            e.printStackTrace();
        }


        board.getCell(1, 2).setLevel(BlockType.DOME);
        board.getCell(1, 4).setLevel(BlockType.DOME);
        board.getCell(2, 2).setLevel(BlockType.LEVEL_TWO);
        board.getCell(2, 4).setLevel(BlockType.LEVEL_THREE);
        board.getCell(3, 0).setLevel(BlockType.DOME);
        board.getCell(3, 1).setLevel(BlockType.DOME);
        board.getCell(3, 2).setLevel(BlockType.LEVEL_TWO);
        board.getCell(3, 4).setLevel(BlockType.LEVEL_TWO);
        board.getCell(4, 0).setLevel(BlockType.DOME);
        board.getCell(4, 2).setLevel(BlockType.DOME);

        assertFalse(board.canMove(player));
        assertTrue(board.canMove(opponent));
    }


    @Test
    public void canBuildTrueTest() {

        board.getCell(4, 1).setLevel(BlockType.LEVEL_TWO);

        try {
            w1.setInitialPosition(2, 3);
            w2.setInitialPosition(4, 1);
            wOpp1.setInitialPosition(1, 3);
            wOpp2.setInitialPosition(3, 3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        board.getCell(1, 2).setLevel(BlockType.DOME);
        board.getCell(1, 4).setLevel(BlockType.DOME);
        board.getCell(2, 2).setLevel(BlockType.LEVEL_TWO);
        board.getCell(2, 4).setLevel(BlockType.LEVEL_THREE);
        board.getCell(3, 0).setLevel(BlockType.DOME);
        board.getCell(3, 1).setLevel(BlockType.DOME);
        board.getCell(3, 2).setLevel(BlockType.LEVEL_TWO);
        board.getCell(3, 4).setLevel(BlockType.LEVEL_TWO);
        board.getCell(4, 0).setLevel(BlockType.DOME);
        board.getCell(4, 2).setLevel(BlockType.DOME);

        assertTrue(board.canBuild(w1));
        assertTrue(board.canBuild(w2));
        assertTrue(board.canBuild(wOpp1));
        assertTrue(board.canBuild(wOpp2));
    }


    @Test
    public void canBuildFalseTest() {

        try {
            w1.setInitialPosition(0, 4);
            w2.setInitialPosition(1, 4);
        } catch (Exception e) {
            e.printStackTrace();
        }

        board.getCell(0, 3).setLevel(BlockType.DOME);
        board.getCell(1, 3).setLevel(BlockType.DOME);
        board.getCell(2, 3).setLevel(BlockType.LEVEL_THREE);
        board.getCell(2, 4).setLevel(BlockType.DOME);

        assertFalse(board.canBuild(w1));
        assertTrue(board.canBuild(w2));
    }

    @Test
    public void invalidCellTest() {
        Board board = new Board();
        assertThrows(InvalidCellException.class, () -> board.getCell(-1, -1));
    }
}