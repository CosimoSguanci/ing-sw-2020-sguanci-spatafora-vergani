package it.polimi.ingsw.controller.commands;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommandTypeTest {

    @Test
    public void isHelperCommandTest() {
        assertTrue(CommandType.isHelperCommandType("help"));
        assertTrue(CommandType.isHelperCommandType("INFO"));
        assertTrue(CommandType.isHelperCommandType("QuiT"));
        assertTrue(CommandType.isHelperCommandType("Turn"));
        assertTrue(CommandType.isHelperCommandType("rules"));
        assertTrue(CommandType.isHelperCommandType("god"));
        assertTrue(CommandType.isHelperCommandType("Board"));
        assertFalse(CommandType.isHelperCommandType("Not an Helper Command"));
    }
}
