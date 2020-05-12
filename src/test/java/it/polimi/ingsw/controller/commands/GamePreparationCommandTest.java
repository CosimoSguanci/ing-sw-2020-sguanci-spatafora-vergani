package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.exceptions.BadCommandException;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GamePreparationCommandTest {

    @Disabled
    @Test
    void setWorkerFirstCell() {
    }

    @Disabled
    @Test
    void setWorkerSecondCell() {
    }

    @Disabled
    @Test
    void getWorkerFirstCell() {
    }

    @Disabled
    @Test
    void getWorkerSecondCell() {
    }

    @Disabled
    @Test
    void handleCommand() {
    }


    @Test
    void parseInputSimpleTest() {
        GamePreparationCommand gamePreparationCommand = GamePreparationCommand.parseInput("place w1 a1 w2 e3");
        GamePreparationCommand expected = new GamePreparationCommand(0, 0, 4, 2);
        boolean equals = expected.workerFirstRow == gamePreparationCommand.workerFirstRow &&
                        expected.workerFirstCol == gamePreparationCommand.workerFirstCol &&
                        expected.workerSecondRow == gamePreparationCommand.workerSecondRow &&
                        expected.workerSecondCol == gamePreparationCommand.workerSecondCol;
        assertTrue(equals);
    }

    @Test
    void parseInputSpaceBarTest() {
        GamePreparationCommand gamePreparationCommand = GamePreparationCommand.parseInput("  PLaCe  w1 C4 W2 b2  ");
        GamePreparationCommand expected = new GamePreparationCommand(2, 3, 1, 1);
        boolean equals = expected.workerFirstRow == gamePreparationCommand.workerFirstRow &&
                expected.workerFirstCol == gamePreparationCommand.workerFirstCol &&
                expected.workerSecondRow == gamePreparationCommand.workerSecondRow &&
                expected.workerSecondCol == gamePreparationCommand.workerSecondCol;
        assertTrue(equals);
    }

    @Test
    void parseInputImpossibleCommandTest() {
        assertThrows(BadCommandException.class, ()->GamePreparationCommand.parseInput("place w2 a1 w1 e3"));
        assertThrows(BadCommandException.class, ()->GamePreparationCommand.parseInput("place W1 A3 w2 a3"));
        assertThrows(BadCommandException.class, ()->GamePreparationCommand.parseInput("place w1 a1 w e3"));
        assertThrows(BadCommandException.class, ()->GamePreparationCommand.parseInput("plaCE w2 a1 w2 e3"));
        assertThrows(BadCommandException.class, ()->GamePreparationCommand.parseInput("place w1 a5 w2 c6"));
        assertThrows(BadCommandException.class, ()->GamePreparationCommand.parseInput("place w1 f3 w2 e3"));
        assertThrows(BadCommandException.class, ()->GamePreparationCommand.parseInput("pLAce w1 d0 w2 e3"));
        assertThrows(BadCommandException.class, ()->GamePreparationCommand.parseInput("select w1 a1 w2 a2"));
        assertThrows(BadCommandException.class, ()->GamePreparationCommand.parseInput("plate w1 a1 W2 e3"));
        assertThrows(BadCommandException.class, ()->GamePreparationCommand.parseInput(" place  w a3 w2   e5"));
        assertThrows(BadCommandException.class, ()->GamePreparationCommand.parseInput("select hera prometheus"));
        assertThrows(BadCommandException.class, ()->GamePreparationCommand.parseInput("place w1 b3 w2 a1 stop"));
        assertThrows(BadCommandException.class, ()->GamePreparationCommand.parseInput("end turn"));
    }
}