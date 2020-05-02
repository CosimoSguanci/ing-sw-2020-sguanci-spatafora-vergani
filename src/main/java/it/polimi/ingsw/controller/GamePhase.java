package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.exceptions.BadCommandException;

import java.util.Arrays;

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

    public static GamePhase parseGamePhase(String input) {
        try {
            return Enum.valueOf(GamePhase.class, input.toUpperCase());
        } catch(IllegalArgumentException e) {
            throw new BadCommandException();
        }
    }


    public static StringBuilder toStringBuilder(){
        String[] values = Arrays.stream(GamePhase.class.getEnumConstants()).map(Enum::name).toArray(String[]::new);
        StringBuilder result = new StringBuilder(values[0].toLowerCase());
        for(int i=1; i<values.length; i++){
            result.append(", ").append(values[i].toLowerCase());
        }
        return result;
    }
}
