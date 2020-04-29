package it.polimi.ingsw.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GamePhaseTest {

    @Test
    void firstPhase() {
        GamePhase gamePhase;
        gamePhase = GamePhase.firstPhase();
        assertEquals(GamePhase.CHOOSE_GODS, gamePhase);
    }

    @Test
    void nextPhase() {
        GamePhase gamePhase;
        gamePhase = GamePhase.firstPhase();
        assertEquals(GamePhase.CHOOSE_GODS, gamePhase);

        try {
            gamePhase = gamePhase.nextPhase();
            assertEquals(gamePhase, GamePhase.GAME_PREPARATION);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}