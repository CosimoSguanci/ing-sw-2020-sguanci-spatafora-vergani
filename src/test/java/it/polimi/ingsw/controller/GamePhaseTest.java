package it.polimi.ingsw.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GamePhaseTest {

    @Test
    void firstPhase() {
        GamePhase gamePhase;
        gamePhase = GamePhase.firstPhase();
        assertEquals(GamePhase.INITIAL_INFO, gamePhase);
    }
}