package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BlockTypeTest {

    @Test
    public void getLevelNumberTest() {
        assertEquals(0, BlockType.GROUND.getLevelNumber());
        assertEquals(1, BlockType.LEVEL_ONE.getLevelNumber());
        assertEquals(2, BlockType.LEVEL_TWO.getLevelNumber());
        assertEquals(3, BlockType.LEVEL_THREE.getLevelNumber());
        assertEquals(4, BlockType.DOME.getLevelNumber());
    }
}