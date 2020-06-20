package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.BadCommandException;

import java.util.ArrayList;


/**
 * GamePhase is the enumeration for all match's phases. The main game-phases are: initial-info,
 * when players decide a nickname and colour; choose-gods, when the god-chooser selects some gods
 * and every player chooses the one he/she wants to play with; game-preparation, when every player
 * places the workers on the board; real-game, when the real match is played; match-ended, the phase
 * that follows a match; match-lost, phase for who lost a match.
 *
 * @author Andrea Mario Vergani
 * @author Cosimo Sguanci
 */
public enum GamePhase {
    INITIAL_INFO, CHOOSE_GODS, GAME_PREPARATION, REAL_GAME, MATCH_ENDED, MATCH_LOST;


    /**
     * This static method simply returns the first phase of a match.
     *
     * @return the (chronological) first phase
     *
     */
    public static GamePhase firstPhase() {
        return INITIAL_INFO;
    }


    /**
     * This static method returns the phase that follows the parameter-phase.
     *
     * @param currentGamePhase the current game phase
     * @return the (chronological) next phase
     * @throws IllegalArgumentException if parameter-phase is not valid or not followed
     *                                  by another phase
     *
     */
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
                throw new IllegalArgumentException();
        }
    }

    /**
     * This static method parses a string and verifies if it represents a valid game-phase.
     *
     * @param input an input string, that should represent a game-phase
     * @return true if the parameter is one of the GamePhases (hypothetical .toString), false
     *         otherwise
     *
     */
    public static boolean isGamePhase(String input) {
        ArrayList<String> values = new ArrayList<>();
        for(GamePhase gamePhase : GamePhase.values()) {
            values.add(gamePhase.toString().toLowerCase());
        }
        //values now contains all the enum values, in the form of String
        return values.contains(input.toLowerCase());
    }


    /**
     * This static method parses a string and verifies which of the valid game-phases it represents.
     *
     * @param input an input string, that should represent a game-phase
     * @return the correspondent GamePhase if the parameter is one of the GamePhases
     *         (hypothetical .toString)
     * @throws BadCommandException if parameter-string does not represent any GamePhase
     *
     */
    public static GamePhase parseGamePhase(String input) {
        try {
            return Enum.valueOf(GamePhase.class, input.toUpperCase());
        } catch(IllegalArgumentException e) {
            throw new BadCommandException();
        }
    }

    /**
     * This static method returns a StringBuilder containing all the game-phases that must be
     * printed, in the form "gamePhase1, gamePhase2, ..."
     *
     * @return all the game-phases that must be printed, in the form "gamePhase1, gamePhase2, ..."
     *
     */
    public static StringBuilder toStringBuilder() {
        int j;
        GamePhase[] phases = GamePhase.class.getEnumConstants();
        StringBuilder result = new StringBuilder();
        for(j=0; j<phases.length; j++) {
            if(phases[j].isPrintable()) {
                result.append(phases[j].toString().toLowerCase());
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

    /**
     * This static method verifies if the caller is a printable game-phase (so it must be printed
     * as a string in some help command or something like this).
     *
     * @return true if the caller is printable, false otherwise
     *
     */
    public boolean isPrintable() {
        return this != MATCH_ENDED && this != MATCH_LOST;
    }
}
