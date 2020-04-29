package it.polimi.ingsw.controller;

public enum GamePhase {
    CHOOSE_GODS, GAME_PREPARATION, REAL_GAME;

    public static GamePhase firstPhase() {
        return CHOOSE_GODS;
    }

    public GamePhase nextPhase() throws Exception {
        if (this == CHOOSE_GODS) {
            return GAME_PREPARATION;
        }
        else if(this == GAME_PREPARATION) {
            return REAL_GAME;
        }
        else{
            throw new Exception();  //TODO decide what to do with this exception
        }
    }
}
