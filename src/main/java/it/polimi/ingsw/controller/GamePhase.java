package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.controller.commands.GamePreparationCommand;
import it.polimi.ingsw.exceptions.BadCommandException;

import java.util.ArrayList;

public enum GamePhase {
    INITIAL_INFO, CHOOSE_GODS, GAME_PREPARATION, REAL_GAME, MATCH_ENDED, MATCH_LOST;

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
                return MATCH_ENDED;
            default:
                // TODO handle this (exception?)
                return REAL_GAME;
        }
    }

    public static boolean isGamePhase(String input) {
        ArrayList<String> values = new ArrayList();
        for(GamePhase gamePhase : GamePhase.values()) {
            values.add(gamePhase.toString().toLowerCase());
        }
        //values now contains all the enum values, in the form of String
        return values.contains(input.toLowerCase());
    }

    public static GamePhase parseGamePhase(String input) {
        try {
            return Enum.valueOf(GamePhase.class, input.toUpperCase());
        } catch(IllegalArgumentException e) {
            throw new BadCommandException();
        }
    }


    public static StringBuilder toStringBuilder() {
        int j;
        GamePhase[] phases = GamePhase.class.getEnumConstants();
        StringBuilder result = new StringBuilder();
        for(j=0; j<phases.length; j++) {
            if(phases[j].isPrintable()) {
                result = result.append(phases[j].toString().toLowerCase());
                break;
            }
        }
        for(int i = j+1; i<phases.length; i++) {
            if(phases[i].isPrintable()) {
                result.append(", ").append(phases[i].toString().toLowerCase());
            }
        }
        return result;
    }

    public boolean isPrintable() {
        return this != MATCH_ENDED && this != MATCH_LOST;
    }
}
