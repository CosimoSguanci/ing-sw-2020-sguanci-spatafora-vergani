package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.BadCommandException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GamePhaseTest {

    @Test
    void firstPhase() {
        GamePhase gamePhase = GamePhase.firstPhase();
        assertEquals(GamePhase.INITIAL_INFO, gamePhase);
    }

    @Test
    void nextGamePhaseTest() {
        GamePhase gamePhase = GamePhase.firstPhase();
        assertSame(gamePhase, GamePhase.INITIAL_INFO);
        gamePhase = GamePhase.nextGamePhase(gamePhase);
        assertSame(gamePhase, GamePhase.CHOOSE_GODS);
        gamePhase = GamePhase.nextGamePhase(gamePhase);
        assertSame(gamePhase, GamePhase.GAME_PREPARATION);
        gamePhase = GamePhase.nextGamePhase(gamePhase);
        assertSame(gamePhase, GamePhase.REAL_GAME);
        gamePhase = GamePhase.nextGamePhase(gamePhase);
        assertSame(gamePhase, GamePhase.MATCH_ENDED);

        assertThrows(IllegalArgumentException.class, () -> {
            GamePhase next = GamePhase.nextGamePhase(GamePhase.MATCH_ENDED);
        });
    }

    @Test
    void isGamePhaseTest() {
        assertTrue(GamePhase.isGamePhase("INITIAL_info"));
        assertTrue(GamePhase.isGamePhase("CHOOSE_gods"));
        assertTrue(GamePhase.isGamePhase("game_preparation"));
        assertTrue(GamePhase.isGamePhase("real_GAME"));
        assertTrue(GamePhase.isGamePhase("match_ended"));
        assertTrue(GamePhase.isGamePhase("MATCH_LOST"));
        assertFalse(GamePhase.isGamePhase("not a valid game phase"));
    }

    @Test
    void isPrintableTest() {
        GamePhase gamePhase = GamePhase.INITIAL_INFO;
        assertTrue(gamePhase.isPrintable());

        gamePhase = GamePhase.nextGamePhase(gamePhase);
        assertTrue(gamePhase.isPrintable());

        gamePhase = GamePhase.nextGamePhase(gamePhase);
        assertTrue(gamePhase.isPrintable());

        gamePhase = GamePhase.nextGamePhase(gamePhase);
        assertTrue(gamePhase.isPrintable());

        gamePhase = GamePhase.nextGamePhase(gamePhase);
        assertFalse(gamePhase.isPrintable());

        gamePhase = GamePhase.MATCH_LOST;
        assertFalse(gamePhase.isPrintable());

    }

    @Test
    void toStringBuilderTest() {
        String phases = "initial_info, choose_gods, game_preparation, real_game";
        assertEquals(phases, GamePhase.toStringBuilder().toString());
    }

    @Test
    void parseGamePhaseTest() {
        assertSame(GamePhase.INITIAL_INFO, GamePhase.parseGamePhase("initIaL_INfo"));
        assertSame(GamePhase.CHOOSE_GODS, GamePhase.parseGamePhase("CHOOSE_gods"));
        assertSame(GamePhase.GAME_PREPARATION, GamePhase.parseGamePhase("game_preparation"));
        assertSame(GamePhase.REAL_GAME, GamePhase.parseGamePhase("real_game"));
        assertSame(GamePhase.MATCH_ENDED, GamePhase.parseGamePhase("MATCH_ENDED"));
        assertSame(GamePhase.MATCH_LOST, GamePhase.parseGamePhase("MATCH_LOST"));

        assertThrows(BadCommandException.class, () -> GamePhase.parseGamePhase("game_prep"));

    }
}