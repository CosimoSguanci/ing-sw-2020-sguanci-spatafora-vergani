package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.BadPlayerCommandException;
import it.polimi.ingsw.model.BlockType;
import it.polimi.ingsw.model.Cell;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerCommandTest {

    @Disabled
    @Test
    void setCellTest() {
    }

    @Disabled
    @Test
    void getCellTest() {
    }

    @Disabled
    @Test
    void getPlayerTest() {
    }

    @Disabled
    @Test
    void setPlayerTest() {
    }

    @Disabled
    @Test
    void getWorkerTest() {
    }

    @Disabled
    @Test
    void setWorkerTest() {
    }

    @Test
    void parseInputEndTest()  {
        PlayerCommand playerCommand = PlayerCommand.parseInput("Player1", "end");
        assertEquals(CommandType.END_TURN, playerCommand.commandType);
    }

    @Test
    void parseInputMoveTest()  {
        PlayerCommand playerCommand = PlayerCommand.parseInput("Player1", "move w2 c4");
        assertEquals("Player1", playerCommand.playerID);
        assertEquals(CommandType.MOVE, playerCommand.commandType);
        assertEquals("w2", playerCommand.workerID);
        assertEquals(2, playerCommand.getCell().getRowIdentifier());
        assertEquals(3, playerCommand.getCell().getColIdentifier());
    }

    @Test
    void parseInputBuildTest()  {
        PlayerCommand playerCommand = PlayerCommand.parseInput("Player1", "buiLD   w1  B2 domE");
        assertEquals("Player1", playerCommand.playerID);
        assertEquals(CommandType.BUILD, playerCommand.commandType);
        assertEquals("w1", playerCommand.workerID);
        assertEquals(1, playerCommand.getCell().getRowIdentifier());
        assertEquals(1, playerCommand.getCell().getColIdentifier());
        assertEquals(BlockType.DOME, playerCommand.cellBlockType);

        playerCommand = PlayerCommand.parseInput("Player2", "build w1 B4 ");
        assertEquals("Player2", playerCommand.playerID);
        assertEquals(CommandType.BUILD, playerCommand.commandType);
        assertEquals("w1", playerCommand.workerID);
        assertEquals(1, playerCommand.getCell().getRowIdentifier());
        assertEquals(3, playerCommand.getCell().getColIdentifier());
        assertNull(playerCommand.cellBlockType);

        playerCommand = PlayerCommand.parseInput("Player1", "buiLD   w1  B2 onE");
        assertEquals("Player1", playerCommand.playerID);
        assertEquals(CommandType.BUILD, playerCommand.commandType);
        assertEquals("w1", playerCommand.workerID);
        assertEquals(1, playerCommand.getCell().getRowIdentifier());
        assertEquals(1, playerCommand.getCell().getColIdentifier());
        assertEquals(BlockType.LEVEL_ONE, playerCommand.cellBlockType);

        playerCommand = PlayerCommand.parseInput("Player1", "buiLD   w1  B2  three");
        assertEquals("Player1", playerCommand.playerID);
        assertEquals(CommandType.BUILD, playerCommand.commandType);
        assertEquals("w1", playerCommand.workerID);
        assertEquals(1, playerCommand.getCell().getRowIdentifier());
        assertEquals(1, playerCommand.getCell().getColIdentifier());
        assertEquals(BlockType.LEVEL_THREE, playerCommand.cellBlockType);
    }


    @Test
    void parseInputStartWithSpaceBarTest()  {
        PlayerCommand playerCommand = PlayerCommand.parseInput("Player1", " buiLD   w1  B2 domE");
        assertEquals("Player1", playerCommand.playerID);
        assertEquals(CommandType.BUILD, playerCommand.commandType);
        assertEquals("w1", playerCommand.workerID);
        assertEquals(1, playerCommand.getCell().getRowIdentifier());
        assertEquals(1, playerCommand.getCell().getColIdentifier());
        assertEquals(BlockType.DOME, playerCommand.cellBlockType);

        playerCommand = PlayerCommand.parseInput("Player5", "    buiLD   w2  A5  ONE");
        assertEquals("Player5", playerCommand.playerID);
        assertEquals(CommandType.BUILD, playerCommand.commandType);
        assertEquals("w2", playerCommand.workerID);
        assertEquals(0, playerCommand.getCell().getRowIdentifier());
        assertEquals(4, playerCommand.getCell().getColIdentifier());
        assertEquals(BlockType.LEVEL_ONE, playerCommand.cellBlockType);
    }

    @Test
    void parseInputEndWithSpaceBarTest()  {
        PlayerCommand playerCommand = PlayerCommand.parseInput("Player1", "buiLD   w1  B2 TwO ");
        assertEquals("Player1", playerCommand.playerID);
        assertEquals(CommandType.BUILD, playerCommand.commandType);
        assertEquals("w1", playerCommand.workerID);
        assertEquals(1, playerCommand.getCell().getRowIdentifier());
        assertEquals(1, playerCommand.getCell().getColIdentifier());
        assertEquals(BlockType.LEVEL_TWO, playerCommand.cellBlockType);
    }

    @Test
    void parseInputInvalidCommandTest()  {
        assertThrows(BadPlayerCommandException.class,
                    () -> PlayerCommand.parseInput("Player1", "buiLD   w3  B2 domE "));
        assertThrows(BadPlayerCommandException.class,
                () -> PlayerCommand.parseInput("Player1", "builw w1 B2 dome"));
        assertThrows(BadPlayerCommandException.class,
                () -> PlayerCommand.parseInput("Player1", "build w1 dome "));
        assertThrows(BadPlayerCommandException.class,
                () -> PlayerCommand.parseInput("Player1", "build w1 A1 dome stop"));
        assertThrows(BadPlayerCommandException.class,
                () -> PlayerCommand.parseInput("Player1", "build w1 F1 dome "));
        assertThrows(BadPlayerCommandException.class,
                () -> PlayerCommand.parseInput("Player1", "build w1 f1 dome "));
        assertThrows(BadPlayerCommandException.class,
                () -> PlayerCommand.parseInput("Player1", "build w1 e6 dome "));
        assertThrows(BadPlayerCommandException.class,
                () -> PlayerCommand.parseInput("Player1", "build w1 e0 dome "));
        assertThrows(BadPlayerCommandException.class,
                () -> PlayerCommand.parseInput("Player1", "move w2 B2 domE"));
        assertThrows(BadPlayerCommandException.class,
                () -> PlayerCommand.parseInput("Player1", "end turn"));
    }
}