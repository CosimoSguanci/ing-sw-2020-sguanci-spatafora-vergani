package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.controller.commands.PlayerCommand;
import it.polimi.ingsw.exceptions.BadCommandException;
import it.polimi.ingsw.model.BlockType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

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
        PlayerCommand playerCommand = PlayerCommand.parseInput("end");
        assertEquals(CommandType.END_TURN, playerCommand.commandType);
    }

    @Test
    void parseInputMoveTest()  {
        PlayerCommand playerCommand = PlayerCommand.parseInput("move w2 c4");
        playerCommand.setPlayerID("Player1");
        assertEquals("Player1", playerCommand.getPlayerID());
        assertEquals(CommandType.MOVE, playerCommand.commandType);
        assertEquals("w2", playerCommand.workerID);
        assertEquals(2, playerCommand.row);
        assertEquals(3, playerCommand.col);
    }

    @Test
    void parseInputBuildTest()  {
        PlayerCommand playerCommand = PlayerCommand.parseInput("buiLD   w1  B2 domE");
        playerCommand.setPlayerID("Player1");
        assertEquals("Player1", playerCommand.getPlayerID());
        assertEquals(CommandType.BUILD, playerCommand.commandType);
        assertEquals("w1", playerCommand.workerID);
        assertEquals(1, playerCommand.row);
        assertEquals(1, playerCommand.col);
        assertEquals(BlockType.DOME, playerCommand.cellBlockType);

        playerCommand = PlayerCommand.parseInput("build w1 B4 ");
        playerCommand.setPlayerID("Player2");
        assertEquals("Player2", playerCommand.getPlayerID());
        assertEquals(CommandType.BUILD, playerCommand.commandType);
        assertEquals("w1", playerCommand.workerID);
        assertEquals(1, playerCommand.row);
        assertEquals(3, playerCommand.col);
        assertNull(playerCommand.cellBlockType);

        playerCommand = PlayerCommand.parseInput("buiLD   w1  B2 onE");
        playerCommand.setPlayerID("Player1");
        assertEquals("Player1", playerCommand.getPlayerID());
        assertEquals(CommandType.BUILD, playerCommand.commandType);
        assertEquals("w1", playerCommand.workerID);
        assertEquals(1, playerCommand.row);
        assertEquals(1, playerCommand.col);
        assertEquals(BlockType.LEVEL_ONE, playerCommand.cellBlockType);

        playerCommand = PlayerCommand.parseInput("buiLD   w1  B2  three");
        playerCommand.setPlayerID("Player1");
        assertEquals("Player1", playerCommand.getPlayerID());
        assertEquals(CommandType.BUILD, playerCommand.commandType);
        assertEquals("w1", playerCommand.workerID);
        assertEquals(1, playerCommand.row);
        assertEquals(1, playerCommand.col);
        assertEquals(BlockType.LEVEL_THREE, playerCommand.cellBlockType);
    }


    @Test
    void parseInputStartWithSpaceBarTest()  {
        PlayerCommand playerCommand = PlayerCommand.parseInput(" buiLD   w1  B2 domE");
        playerCommand.setPlayerID("Player1");
        assertEquals("Player1", playerCommand.getPlayerID());
        assertEquals(CommandType.BUILD, playerCommand.commandType);
        assertEquals("w1", playerCommand.workerID);
        assertEquals(1, playerCommand.row);
        assertEquals(1, playerCommand.col);
        assertEquals(BlockType.DOME, playerCommand.cellBlockType);

        playerCommand = PlayerCommand.parseInput("    buiLD   w2  A5  ONE");
        playerCommand.setPlayerID("Player5");
        assertEquals("Player5", playerCommand.getPlayerID());
        assertEquals(CommandType.BUILD, playerCommand.commandType);
        assertEquals("w2", playerCommand.workerID);
        assertEquals(0, playerCommand.row);
        assertEquals(4, playerCommand.col);
        assertEquals(BlockType.LEVEL_ONE, playerCommand.cellBlockType);
    }

    @Test
    void parseInputEndWithSpaceBarTest()  {
        PlayerCommand playerCommand = PlayerCommand.parseInput("buiLD   w1  B2 TwO ");
        playerCommand.setPlayerID("Player1");
        assertEquals("Player1", playerCommand.getPlayerID());
        assertEquals(CommandType.BUILD, playerCommand.commandType);
        assertEquals("w1", playerCommand.workerID);
        assertEquals(1, playerCommand.row);
        assertEquals(1, playerCommand.col);
        assertEquals(BlockType.LEVEL_TWO, playerCommand.cellBlockType);
    }

    @Test
    void parseInputInvalidCommandTest()  {
        assertThrows(BadCommandException.class,
                    () -> PlayerCommand.parseInput("buiLD   w3  B2 domE "));
        assertThrows(BadCommandException.class,
                () -> PlayerCommand.parseInput("builw w1 B2 dome"));
        assertThrows(BadCommandException.class,
                () -> PlayerCommand.parseInput("build w1 dome "));
        assertThrows(BadCommandException.class,
                () -> PlayerCommand.parseInput("build w1 A1 dome stop"));
        assertThrows(BadCommandException.class,
                () -> PlayerCommand.parseInput("build w1 F1 dome "));
        assertThrows(BadCommandException.class,
                () -> PlayerCommand.parseInput("build w1 f1 dome "));
        assertThrows(BadCommandException.class,
                () -> PlayerCommand.parseInput("build w1 e6 dome "));
        assertThrows(BadCommandException.class,
                () -> PlayerCommand.parseInput("build w1 e0 dome "));
        assertThrows(BadCommandException.class,
                () -> PlayerCommand.parseInput("move w2 B2 domE"));
        assertThrows(BadCommandException.class,
                () -> PlayerCommand.parseInput("end turn"));
        assertThrows(BadCommandException.class,
                () -> PlayerCommand.parseInput("move w2 B2 tree"));
        assertThrows(BadCommandException.class,
                () -> PlayerCommand.parseInput(" "));
        assertThrows(BadCommandException.class,
                () -> PlayerCommand.parseInput(""));
    }
}