package it.polimi.ingsw.model;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        Cell cell = new Cell(1,4);
        assertEquals(BlockType.GROUND, cell.getLevel());
    }

    @Test
    public void increaseLevelTest() /*throws Exception*/ {
        Cell cell = new Cell(2,1);
        cell.increaseLevel();
        assertEquals(BlockType.LEVEL_ONE, cell.getLevel());
        cell.increaseLevel();
        assertEquals(BlockType.LEVEL_TWO, cell.getLevel());
        cell.increaseLevel();
        assertEquals(BlockType.LEVEL_THREE, cell.getLevel());
        cell.increaseLevel();
        assertEquals(BlockType.DOME, cell.getLevel());
    }

    @Ignore  //because we haven't decided what to do in "dome case" (exception or not) yet
    @Test
    public void increaseLevelDomeTest() /*throws Exception*/ {
        Cell cell = new Cell(2,1);
        cell.increaseLevel();
        assertEquals(BlockType.LEVEL_ONE, cell.getLevel());
        cell.increaseLevel();
        assertEquals(BlockType.LEVEL_TWO, cell.getLevel());
        cell.increaseLevel();
        assertEquals(BlockType.LEVEL_THREE, cell.getLevel());
        cell.increaseLevel();
        assertEquals(BlockType.DOME, cell.getLevel());
        assertThrows(Exception.class, cell::increaseLevel);
    }

    @Test
    public void setLevelTest() {
        Cell cell = new Cell(3,3);
        cell.setLevel(BlockType.DOME);
        assertEquals(BlockType.DOME, cell.getLevel());
    }

    @Test
    public void isLevelDifferenceOkTest() /*throws Exception*/ {
        Cell cell = new Cell(4,2);
        cell.increaseLevel();
        cell.increaseLevel();  //cell is level two
        Cell c = new Cell(0,3);  //c is ground level

        assertTrue(cell.isLevelDifferenceOk(c));
        assertFalse(c.isLevelDifferenceOk(cell));
        assertTrue(cell.isLevelDifferenceOk(cell));

        c.increaseLevel();  //c is level one
        assertTrue(cell.isLevelDifferenceOk(c));
        assertTrue(c.isLevelDifferenceOk(cell));

        Cell t = new Cell(1,1);
        t.increaseLevel();
        t.increaseLevel();
        t.increaseLevel();  //t is level three
        assertFalse(c.isLevelDifferenceOk(t));
        assertTrue(cell.isLevelDifferenceOk(t));
    }


    @Test
    public void levelDifferenceTest() /*throws Exception*/ {
        Cell cell = new Cell(4,2);
        cell.increaseLevel();
        cell.increaseLevel();  //cell is level two
        Cell c = new Cell(0,3);  //c is ground level

        assertEquals(2, cell.levelDifference(c));
        assertEquals(-2, c.levelDifference(cell));
        assertEquals(0, cell.levelDifference(cell));

        c.increaseLevel();  //c is level one
        assertEquals(1, cell.levelDifference(c));
        assertEquals(-1, c.levelDifference(cell));

        Cell t = new Cell(1,1);
        t.increaseLevel();
        t.increaseLevel();
        t.increaseLevel();  //t is level three
        assertEquals(-2, c.levelDifference(t));
        assertEquals(-1, cell.levelDifference(t));
    }

    @Test
    public void getRowIdentifier() {
        Cell c1 = new Cell(1,3);
        Cell c2 = new Cell(4,2);
        Cell c3 = new Cell(3,0);

        assertEquals(1, c1.getRowIdentifier());
        assertEquals(4, c2.getRowIdentifier());
        assertEquals(3, c3.getRowIdentifier());
    }

    @Test
    public void getColIdentifier() {
        Cell c1 = new Cell(1,3);
        Cell c2 = new Cell(4,2);
        Cell c3 = new Cell(3,0);

        assertEquals(3, c1.getColIdentifier());
        assertEquals(2, c2.getColIdentifier());
        assertEquals(0, c3.getColIdentifier());
    }

    @Test
    public void isAdjacentToTest() {
        Cell c1 = new Cell(3,1);
        Cell c2 = new Cell(4,1);
        assertTrue(c1.isAdjacentTo(c2));
        assertTrue(c2.isAdjacentTo(c1));

        Cell c3 = new Cell(1,4);
        Cell c4 = new Cell(0,3);
        assertTrue(c3.isAdjacentTo(c4));
        assertTrue(c4.isAdjacentTo(c3));

        Cell c5 = new Cell(0,0);
        Cell c6 = new Cell(1,1);
        assertTrue(c5.isAdjacentTo(c6));
        assertTrue(c6.isAdjacentTo(c5));

        Cell c7 = new Cell(0,3);
        Cell c8 = new Cell(1,1);
        assertFalse(c7.isAdjacentTo(c8));
        assertFalse(c8.isAdjacentTo(c7));

        Cell c9 = new Cell(3,2);
        Cell c10 = new Cell(1,3);
        assertFalse(c9.isAdjacentTo(c10));
        assertFalse(c10.isAdjacentTo(c9));
    }

}