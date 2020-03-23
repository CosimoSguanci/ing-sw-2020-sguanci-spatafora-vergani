package it.polimi.ingsw.model;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class CellTest {

    @Ignore
    @Test
    public void isEmptyTest() {
    }

    @Ignore
    @Test
    public void getWorkerTest() {
    }

    @Test
    public void getLevelGroundTest() {
        Cell cell = new Cell();
        assertEquals(BlockType.GROUND, cell.getLevel());
    }

    @Test
    public void increaseLevelTest() throws Exception {
        Cell cell = new Cell();
        cell.increaseLevel();
        assertEquals(BlockType.LEVEL_ONE, cell.getLevel());
        cell.increaseLevel();
        assertEquals(BlockType.LEVEL_TWO, cell.getLevel());
        cell.increaseLevel();
        assertEquals(BlockType.LEVEL_THREE, cell.getLevel());
        cell.increaseLevel();
        assertEquals(BlockType.DOME, cell.getLevel());
    }

    @Test
    public void levelDifferenceTest() throws Exception {
        Cell cell = new Cell();
        cell.increaseLevel();
        cell.increaseLevel();  //cell is level two
        Cell c = new Cell();  //c is ground level

        assertEquals(2, cell.levelDifference(c));
        assertEquals(-2, c.levelDifference(cell));
        assertEquals(0, cell.levelDifference(cell));

        c.increaseLevel();  //c is level one
        assertEquals(1, cell.levelDifference(c));
        assertEquals(-1, c.levelDifference(cell));
    }
}