package it.polimi.ingsw.controller;

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
    void parseInputEndTest() throws Exception {
        PlayerCommand playerCommand = PlayerCommand.parseInput("Player1", "end");
        assertEquals(CommandType.END_TURN, playerCommand.commandType);
    }
}