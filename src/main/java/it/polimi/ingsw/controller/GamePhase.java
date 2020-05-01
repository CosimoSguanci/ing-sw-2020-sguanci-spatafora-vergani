package it.polimi.ingsw.controller;

public enum GamePhase {
    INITIAL_INFO, CHOOSE_GODS, GAME_PREPARATION, REAL_GAME;

    public static GamePhase firstPhase() {
        return INITIAL_INFO;
    }

    public static GamePhase nextGamePhase(GamePhase currentGamePhase) {
        switch(currentGamePhase) {
            case INITIAL_INFO:
                return CHOOSE_GODS;
            case CHOOSE_GODS:
                return GAME_PREPARATION;
            case GAME_PREPARATION:
                return REAL_GAME;
            case REAL_GAME:
                // TODO handle this
                return REAL_GAME;
            default:
                // TODO handle this (exception?)
                return REAL_GAME;
        }
    }
}
