package it.polimi.ingsw.controller;

/**
 * CommandType is the enumeration for the type of command a player can do.
 * In Santorini there are three type of possible commands: MOVE used to
 * move a player's worker to a different cell; BUILD used to build in a cell
 * according to relatives restrictions, ENDTURN to declare the termination
 * of the turn and then the turn pass to another player.
 */
public enum CommandType {

    MOVE, BUILD, END_TURN;
}
