package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BlockTypeTest {

    @Test
    public void getLevelNumberTest() {
        assertEquals(0, BlockType.GROUND.getLevelNumber());
        assertEquals(1, BlockType.LEVEL_ONE.getLevelNumber());
        assertEquals(2, BlockType.LEVEL_TWO.getLevelNumber());
        assertEquals(3, BlockType.LEVEL_THREE.getLevelNumber());
        assertEquals(4, BlockType.DOME.getLevelNumber());
    }

    @Test
    public void convertBlockTypeToTextTest() {
        assertEquals("0", BlockType.convertBlockTypeToText(BlockType.GROUND));
        assertEquals("1", BlockType.convertBlockTypeToText(BlockType.LEVEL_ONE));
        assertEquals("2", BlockType.convertBlockTypeToText(BlockType.LEVEL_TWO));
        assertEquals("3", BlockType.convertBlockTypeToText(BlockType.LEVEL_THREE));
        assertEquals("D", BlockType.convertBlockTypeToText(BlockType.DOME));
    }
}