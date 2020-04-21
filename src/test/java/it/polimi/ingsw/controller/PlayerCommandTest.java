package it.polimi.ingsw.controller;

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
    void parseInputEndTest() throws Exception {
        PlayerCommand playerCommand = PlayerCommand.parseInput("Player1", "end");
        assertEquals(CommandType.END_TURN, playerCommand.commandType);
    }

    @Test
    void parseInputMoveTest() throws Exception {
        PlayerCommand playerCommand = PlayerCommand.parseInput("Player1", "move w2 c4");
        assertEquals("Player1", playerCommand.playerID);
        assertEquals(CommandType.MOVE, playerCommand.commandType);
        assertEquals("w2", playerCommand.workerID);
        assertEquals(2, playerCommand.getCell().getRowIdentifier());
        assertEquals(3, playerCommand.getCell().getColIdentifier());
    }

    @Test
    void parseInputBuildTest() throws Exception {
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
        assertEquals(null, playerCommand.cellBlockType);
    }

    @Disabled
    @Test
    void parseInputStartWithBackSpaceTest() throws Exception {
        PlayerCommand playerCommand = PlayerCommand.parseInput("Player1", " buiLD   w1  B2 domE");
        assertEquals("Player1", playerCommand.playerID);
        assertEquals(CommandType.BUILD, playerCommand.commandType);
        assertEquals("w1", playerCommand.workerID);
        assertEquals(1, playerCommand.getCell().getRowIdentifier());
        assertEquals(1, playerCommand.getCell().getColIdentifier());
        assertEquals(BlockType.DOME, playerCommand.cellBlockType);
    }

    @Test
    void parseInputEndWithBackSpaceTest() throws Exception {
        PlayerCommand playerCommand = PlayerCommand.parseInput("Player1", "buiLD   w1  B2 domE ");
        assertEquals("Player1", playerCommand.playerID);
        assertEquals(CommandType.BUILD, playerCommand.commandType);
        assertEquals("w1", playerCommand.workerID);
        assertEquals(1, playerCommand.getCell().getRowIdentifier());
        assertEquals(1, playerCommand.getCell().getColIdentifier());
        assertEquals(BlockType.DOME, playerCommand.cellBlockType);
    }

    @Test
    void parseInputInvalidCommandTest() throws Exception {
        assertThrows(Exception.class,
                    () -> PlayerCommand.parseInput("Player1", "buiLD   w3  B2 domE "));
        assertThrows(Exception.class,
                () -> PlayerCommand.parseInput("Player1", "builw w1 B2 dome"));
        assertThrows(Exception.class,
                () -> PlayerCommand.parseInput("Player1", "build w1 dome "));
        assertThrows(Exception.class,
                () -> PlayerCommand.parseInput("Player1", "build w1 A1 dome stop"));
        assertThrows(Exception.class,
                () -> PlayerCommand.parseInput("Player1", "build w1 F1 dome "));
        assertThrows(Exception.class,
                () -> PlayerCommand.parseInput("Player1", "build w1 f1 dome "));
        assertThrows(Exception.class,
                () -> PlayerCommand.parseInput("Player1", "build w1 e6 dome "));
        assertThrows(Exception.class,
                () -> PlayerCommand.parseInput("Player1", "build w1 e0 dome "));
        assertThrows(Exception.class,
                () -> PlayerCommand.parseInput("Player1", "move w2 B2 domE"));
        assertThrows(Exception.class,
                () -> PlayerCommand.parseInput("Player1", "end turn"));
    }
}